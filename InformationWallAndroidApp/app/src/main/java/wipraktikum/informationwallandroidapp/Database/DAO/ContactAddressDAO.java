package wipraktikum.informationwallandroidapp.Database.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.ContactAddress;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBContactAddress;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Remi on 28.10.2015.
 */
public class ContactAddressDAO implements IDAO {
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
            List<DBContactAddress> dbContactAddresses = InfoWallApplication.getInstance().getDatabaseHelper().getContactAdressDAO().queryForAll();
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
            DBContactAddress dbContactAddress = InfoWallApplication.getInstance().getDatabaseHelper().getContactAdressDAO().queryForId(iD);
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
            InfoWallApplication.getInstance().getDatabaseHelper().getContactAdressDAO().create(mapContactAddressToDBContractAddress((ContactAddress) object));
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
            InfoWallApplication.getInstance().getDatabaseHelper().getContactAdressDAO().update(mapContactAddressToDBContractAddress((ContactAddress) object));
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
            InfoWallApplication.getInstance().getDatabaseHelper().getContactAdressDAO().deleteById(Id);
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public ContactAddress mapDBContactAddressToContactAddress(DBContactAddress dbContactAddress) {
        ContactAddress contactAddress = new ContactAddress();

        contactAddress.setCity(dbContactAddress.getCity());
        contactAddress.setContactAddressID(dbContactAddress.getContactAddressID());
        contactAddress.setHouseNumber(dbContactAddress.getHouseNumber());
        contactAddress.setZipCode(dbContactAddress.getZipCode());

        return contactAddress;
    }
    public DBContactAddress mapContactAddressToDBContractAddress(ContactAddress contactAddress) {
        DBContactAddress dbContactAddress = new DBContactAddress();
        dbContactAddress.setCity(contactAddress.getCity());
        dbContactAddress.setContactAddressID(contactAddress.getContactAddressID());
        dbContactAddress.setHouseNumber(contactAddress.getHouseNumber());
        dbContactAddress.setZipCode(contactAddress.getZipCode());

        return dbContactAddress;
    }
}
