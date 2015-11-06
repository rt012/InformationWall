package wipraktikum.informationwallandroidapp.Database.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact;
import wipraktikum.informationwallandroidapp.BusinessObject.ContactAddress;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBContact;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBContactAddress;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public class ContactDAO implements IDAO {
    private static ContactDAO instance = null;

    private ContactDAO(){}

    public static ContactDAO getInstance(){
        if (instance == null){
            instance = new ContactDAO();
        }
        return instance;
    }

    @Override
    public ArrayList queryForAll() {
        ArrayList<Contact> contactAddresses = new ArrayList<>();;
        try {
            List<DBContact> dbContactAddresses = InfoWallApplication.getInstance().getDatabaseHelper().getContactDAO().queryForAll();
            for(DBContact dbContact : dbContactAddresses){
                contactAddresses.add(mapDBContactToContact(dbContact));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactAddresses;
    }

    @Override
    public Object queryForId(long iD) {
        Contact contact = null;
        try {
            DBContact dbContact = InfoWallApplication.getInstance().getDatabaseHelper().getContactDAO().queryForId(iD);
            contact = mapDBContactToContact(dbContact);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }

    @Override
    public boolean create(Object object) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getContactDAO().create(mapContactToDBContact((Contact) object));
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
            InfoWallApplication.getInstance().getDatabaseHelper().getContactDAO().update(mapContactToDBContact((Contact) object));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean delete(Object object) {
        return false;
    }

    @Override
    public boolean deleteByID(long Id) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getContactDAO().deleteById(Id);
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public Contact mapDBContactToContact(DBContact dbContact) {
        Contact contact = new Contact();

        if (dbContact != null) {
            contact.setCompany(dbContact.getCompany());
            contact.setContactID(dbContact.getmContactID());
            contact.setEMailAddress(dbContact.getEMailAddress());
            contact.setName(dbContact.getName());
            contact.setSurname(dbContact.getSurname());
            contact.setTelephone(dbContact.getTelephone());
            contact.setContactAddress((ContactAddress) DAOHelper.getInstance().
                    getContactAddressDAO().mapDBContactAddressToContactAddress(dbContact.getContactAddress()));
            contact.setSyncStatus(dbContact.isSyncStatus());
        }
        return contact;
    }

    public DBContact mapContactToDBContact(Contact contact) {
        DBContact dbContact = new DBContact();

        if (contact != null) {
            dbContact.setTelephone(contact.getTelephone());
            dbContact.setSurname(contact.getSurname());
            dbContact.setName(contact.getName());
            dbContact.setEMailAddress(contact.getEMailAddress());
            dbContact.setCompany(contact.getCompany());
            dbContact.setContactID(contact.getContactID());
            dbContact.setContactAddress((DBContactAddress) DAOHelper.getInstance().
                    getContactAddressDAO().mapContactAddressToDBContractAddress(contact.getContactAddress()));
            dbContact.setSyncStatus(contact.isSyncStatus());
        }
        return dbContact;
    }
}
