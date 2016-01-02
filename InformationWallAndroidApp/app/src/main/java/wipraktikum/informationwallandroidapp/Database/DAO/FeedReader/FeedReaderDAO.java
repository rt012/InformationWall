package wipraktikum.informationwallandroidapp.Database.DAO.FeedReader;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.FeedReader.DBFeed;
import wipraktikum.informationwallandroidapp.Database.DAO.DAO;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 28.12.2015.
 */
public class FeedReaderDAO  implements DAO{
    private static FeedReaderDAO instance = null;

    private FeedReaderDAO(){}

    public static FeedReaderDAO getInstance(){
        if (instance == null){
            instance = new FeedReaderDAO();
        }
        return instance;
    }

    @Override
    public ArrayList queryForAll() {
        ArrayList<Feed> feeds = new ArrayList<>();;
        try {
            List<DBFeed> dbFeeds = InfoWallApplication.getInstance().getDatabaseHelper().getFeedReaderDAO().queryForAll();
            for(DBFeed dbFeed : dbFeeds){
                feeds.add(mapDBFeedToFeed(dbFeed));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feeds;
    }

    @Override
    public Object queryForId(long iD) {
        Feed feed = null;
        try {
            DBFeed dbFeed = InfoWallApplication.getInstance().getDatabaseHelper().getFeedReaderDAO().queryForId(iD);
            feed = mapDBFeedToFeed(dbFeed);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feed;
    }

    @Override
    public boolean create(Object object) {
        boolean ok = false;
        try {
            Feed feed = (Feed) object;
            feed = assignUnusedId(feed);
            InfoWallApplication.getInstance().getDatabaseHelper().getFeedReaderDAO().create(mapFeedToDBFeed(feed));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    private Feed assignUnusedId(Feed feed){
        try {
            if (!InfoWallApplication.getInstance().getDatabaseHelper().getFeedReaderDAO().idExists(feed.getFeedReaderID())) return feed;
            else feed.setFeedReaderID(feed.getFeedReaderID() + 1);
            feed = assignUnusedId(feed);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feed;
    }

    @Override
    public boolean update(Object object){
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getFeedReaderDAO().createOrUpdate(mapFeedToDBFeed((Feed) object));
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
            Dao<DBFeed, Long> feedDAO = InfoWallApplication.getInstance().getDatabaseHelper().getFeedReaderDAO();
            Feed feed = (Feed) object;
            feedDAO.createOrUpdate(mapFeedToDBFeed(feed));

            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean delete(Object object) {
        Feed feed = (Feed) object;
        deleteByID(feed.getFeedReaderID());
        return false;
    }

    @Override
    public boolean deleteByID(long Id) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getFeedReaderDAO().deleteById(Id);
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public ArrayList<Feed> getUnsyncedItems() throws SQLException{
        Dao<DBFeed, Long> feedReaderDAO = InfoWallApplication.getInstance().getDatabaseHelper().getFeedReaderDAO();
        // get our query builder from the DAO
        QueryBuilder<DBFeed, Long> queryBuilder =
                feedReaderDAO.queryBuilder();
        queryBuilder.where().eq(DBFeed.SYNCSTATUS_FIELD_NAME, false);
        PreparedQuery<DBFeed> preparedQuery = queryBuilder.prepare();
        List<DBFeed> dbFeeds = feedReaderDAO.query(preparedQuery);

        ArrayList<Feed> unsycedFeeds = new ArrayList<Feed>();
        if(dbFeeds != null || !dbFeeds.isEmpty()) {
            for(DBFeed dbFeed : dbFeeds) {
                unsycedFeeds.add(mapDBFeedToFeed(dbFeed));
            }
        }
        return unsycedFeeds;
    }


    public Feed mapDBFeedToFeed(DBFeed dbFeed) {
        Feed feed = new Feed();

        if (dbFeed != null) {
            feed.setImageURL(dbFeed.getImageURL());
            feed.setTitle(dbFeed.getTitle());
            feed.setDescription(dbFeed.getDescription());
            feed.setFeed(dbFeed.getFeed());
            feed.setFeedReaderID(dbFeed.getFeedReaderID());
            feed.setWebsite(dbFeed.getWebsite());
            feed.setSyncStatus(dbFeed.isSyncStatus());
        }

        return feed;
    }

    public DBFeed mapFeedToDBFeed(Feed feed) {
        DBFeed dbFeed = new DBFeed();

        if (dbFeed != null) {
            dbFeed.setImageURL(feed.getImageURL());
            dbFeed.setTitle(feed.getTitle());
            dbFeed.setDescription(feed.getDescription());
            dbFeed.setFeed(feed.getFeed());
            dbFeed.setFeedReaderID(feed.getFeedReaderID());
            dbFeed.setWebsite(feed.getWebsite());
            dbFeed.setSyncStatus(feed.isSyncStatus());
        }

        return dbFeed;
    }
}
