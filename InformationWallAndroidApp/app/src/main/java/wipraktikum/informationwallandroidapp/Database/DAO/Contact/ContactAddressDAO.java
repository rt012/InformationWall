package wipraktikum.informationwallandroidapp.Database.DAO.Contact;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact.ContactAddress;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.Contact.DBContactAddress;
import wipraktikum.informationwallandroidapp.Database.DAO.DAO;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Remi on 28.10.2015.
 */
public class ContactAddressDAO implements DAO {
    private static ContactAddressDAO instance = null;

    private ContactAddressDAO(){}

    public static ContactAddressDAO getInstance(){
        if (instance == null){
            instance = new ContactAddressDAO();
        }
        return instance;
    }

    @Override
    public ArrayList queryForAll() {
        ArrayList<ContactAddress> contactAddresses = new ArrayList<>();;
        try {
            List<DBContactAddress> dbContactAddresses = InfoWallApplication.getInstance().getDatabaseHelper().getContactAddressDAO().queryForAll();
            for(DBContactAddress dbContactAddress : dbContactAddresses){
                contactAddresses.add(mapDBContactAddressToContactAddress(dbContactAddress));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactAddresses;
    }

    @Override
    public Object queryForId(long iD) {
        ContactAddress contactAddress = null;
        try {
            DBContactAddress dbContactAddress = InfoWallApplication.getInstance().getDatabaseHelper().getContactAddressDAO().queryForId(iD);
            contactAddress = mapDBContactAddressToContactAddress(dbContactAddress);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactAddress;
    }

    @Override
    public boolean create(Object object) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getContactAddressDAO().create(mapContactAddressToDBContractAddress((ContactAddress) object));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean update(Object object){
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getContactAddressDAO().update(mapContactAddressToDBContractAddress((ContactAddress) object));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public boolean createOrUpdate(Object object) {
        boolean ok = false;
        if (object == null) return ok;
        try {
            Dao<DBContactAddress, Long> contactAddressDAO = InfoWallApplication.getInstance().getDatabaseHelper().getContactAddressDAO();
            ContactAddress contactAddress = (ContactAddress) object;
            contactAddressDAO.createOrUpdate(mapContactAddressToDBContractAddress(contactAddress));

            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean delete(Object object) {
        boolean ok = false;
        // TODO falls n�tig
        return ok;
    }

    @Override
    public boolean deleteByID(long Id) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getContactAddressDAO().deleteById(Id);
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public ContactAddress mapDBContactAddressToContactAddress(DBContactAddress dbContactAddress) {
        ContactAddress contactAddress = new ContactAddress();

        if (dbContactAddress != null) {
            contactAddress.setCity(dbContactAddress.getCity());
            contactAddress.setContactAddressID(dbContactAddress.getContactAddressID());
            contactAddress.setStreetName(dbContactAddress.getStreetName());
            contactAddress.setHouseNumber(dbContactAddress.getHouseNumber());
            contactAddress.setZipCode(dbContactAddress.getZipCode());
            contactAddress.setSyncStatus(dbContactAddress.isSyncStatus());
        }
        return contactAddress;
    }
    public DBContactAddress mapContactAddressToDBContractAddress(ContactAddress contactAddress) {
        DBContactAddress dbContactAddress = new DBContactAddress();

        if (contactAddress != null) {
            dbContactAddress.setCity(contactAddress.getCity());
            dbContactAddress.setContactAddressID(contactAddress.getContactAddressID());
            dbContactAddress.setStreetName(contactAddress.getStreetName());
            dbContactAddress.setHouseNumber(contactAddress.getHouseNumber());
            dbContactAddress.setZipCode(contactAddress.getZipCode());
            dbContactAddress.setSyncStatus(contactAddress.isSyncStatus());
        }
        return dbContactAddress;
    }
}
