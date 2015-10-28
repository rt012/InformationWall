package wipraktikum.informationwallandroidapp.Database.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.ContactAddress;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBContactAddress;
import wipraktikum.informationwallandroidapp.Database.IDAO;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Remi on 28.10.2015.
 */
public class ContactAddressDAO implements IDAO {
    @Override
    public ArrayList queryForAll() {
        ArrayList<ContactAddress> contactAddresses = null;
        try {
            List<DBContactAddress> dbContactAddresses = InfoWallApplication.getInstance().getDatabaseHelper().getContactAdressDAO().queryForAll();
            for(DBContactAddress dbContactAddresse : dbContactAddresses){
                contactAddresses.add(mappDBContactAddressToContactAddress(dbContactAddresse));
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
            contactAddress = mappDBContactAddressToContactAddress(dbContactAddress);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactAddress;
    }

    @Override
    public boolean create(Object object) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getContactAdressDAO().create(mappContactAdressToDBContractAdress((ContactAddress) object));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean delete(Object object) {
        boolean ok = false;
        // TODO falls nötig
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


    private ContactAddress mappDBContactAddressToContactAddress(DBContactAddress dbContactAddress) {
        ContactAddress contactAdress = new ContactAddress();
        contactAdress.setmCity(dbContactAddress.getmCity());
        contactAdress.setmContactAddressID(dbContactAddress.getmContactAddressID());
        contactAdress.setmHouseNumber(dbContactAddress.getmHouseNumber());
        contactAdress.setmZipCode(dbContactAddress.getmZipCode());

        return contactAdress;
    }
    private DBContactAddress mappContactAdressToDBContractAdress(ContactAddress contactAddress) {

        DBContactAddress dbContactAdress = new DBContactAddress();
        dbContactAdress.setmCity(contactAddress.getmCity());
        dbContactAdress.setmContactAddressID(contactAddress.getmContactAddressID());
        dbContactAdress.setmHouseNumber(contactAddress.getmHouseNumber());
        dbContactAdress.setmZipCode(contactAddress.getmZipCode());

        return dbContactAdress;
    }
}
