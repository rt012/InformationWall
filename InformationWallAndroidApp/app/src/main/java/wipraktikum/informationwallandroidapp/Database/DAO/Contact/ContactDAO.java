package wipraktikum.informationwallandroidapp.Database.DAO.Contact;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact.Contact;
import wipraktikum.informationwallandroidapp.BusinessObject.Contact.ContactAddress;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.Contact.DBContact;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.Contact.DBContactAddress;
import wipraktikum.informationwallandroidapp.Database.DAO.DAO;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public class ContactDAO implements DAO {
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
            InfoWallApplication.getInstance().getDatabaseHelper().getContactDAO().createOrUpdate(mapContactToDBContact((Contact) object));
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
            InfoWallApplication.getInstance().getDatabaseHelper().getContactDAO().createOrUpdate(mapContactToDBContact((Contact) object));
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
            Dao<DBContact, Long> contactDAO = InfoWallApplication.getInstance().getDatabaseHelper().getContactDAO();
            Contact contact = (Contact) object;
            DAOHelper.getContactAddressDAO().createOrUpdate(contact.getContactAddress());
            contactDAO.createOrUpdate(mapContactToDBContact(contact));

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

    public ArrayList<Contact> getUnsyncedItems() throws SQLException{
        Dao<DBContact, Long> contactDAO = InfoWallApplication.getInstance().getDatabaseHelper().getContactDAO();
        // get our query builder from the DAO
        QueryBuilder<DBContact, Long> queryBuilder =
                contactDAO.queryBuilder();
        queryBuilder.where().eq(DBContact.SYNCSTATUS_FIELD_NAME, false);
        PreparedQuery<DBContact> preparedQuery = queryBuilder.prepare();
        List<DBContact> dbContacts = contactDAO.query(preparedQuery);

        ArrayList<Contact> unsyncedContactList = new ArrayList<Contact>();
        if(dbContacts != null || !dbContacts.isEmpty()) {
            for(DBContact dbContact : dbContacts) {
                unsyncedContactList.add(mapDBContactToContact(dbContact));
            }
        }
        return unsyncedContactList;
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
            contact.setContactAddress((ContactAddress) DAOHelper.
                    getContactAddressDAO().mapDBContactAddressToContactAddress(dbContact.getContactAddress()));
            contact.setSyncStatus(dbContact.isSyncStatus());
        }else{
            //create a empty ContactAddress
            contact.setContactAddress(new ContactAddress());
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
            dbContact.setContactAddress((DBContactAddress) DAOHelper.
                    getContactAddressDAO().mapContactAddressToDBContractAddress(contact.getContactAddress()));
            dbContact.setSyncStatus(contact.isSyncStatus());
        }else{
            //create a empty ContactAddress
            dbContact.setContactAddress(new DBContactAddress());
        }

        return dbContact;
    }
}
