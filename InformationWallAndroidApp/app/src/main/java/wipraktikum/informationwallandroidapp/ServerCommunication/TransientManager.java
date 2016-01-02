package wipraktikum.informationwallandroidapp.ServerCommunication;

import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;

/**
 * Created by Eric Schmidt on 02.12.2015.
 */
public class TransientManager {
    public static User keepTransientUserData(User serverUser) {
        if (serverUser != null) {
            User clientUser = (User) DAOHelper.getUserDAO()
                    .queryForId(serverUser.getUserID());
            if (clientUser != null) {
                serverUser.setServerURL(clientUser.getServerURL());
                serverUser.setKeepLogInData(clientUser.isKeepLogInData());
                serverUser.setPreviousLoggedIn(clientUser.isPreviousLoggedIn());
                serverUser.setLoggedIn(clientUser.isLoggedIn());
            }
        }
        return serverUser;
    }

    public static List<BlackBoardAttachment> keepTransientAttachmentList(List<BlackBoardAttachment> serverBlackboardAttachments) {
        if (serverBlackboardAttachments != null) {
            for (BlackBoardAttachment serverBlackboardAttachment : serverBlackboardAttachments) {
                BlackBoardAttachment clientBlackboardAttachment = (BlackBoardAttachment)
                        DAOHelper.getBlackBoardAttachmentDAO().queryForRemoteDataPath(serverBlackboardAttachment.getRemoteDataPath());
                if (clientBlackboardAttachment != null) {
                    serverBlackboardAttachment.setDeviceDataPath(clientBlackboardAttachment.getDeviceDataPath());
                }
            }
        }
        return serverBlackboardAttachments;
    }

    public static Feed keepTransientFeedData(Feed serverFeed){
        if (serverFeed != null){
            Feed clientFeed = (Feed) DAOHelper.getFeedReaderDAO().queryForId(serverFeed.getFeedReaderID());
            if (clientFeed != null) {
                serverFeed.setWebsite(clientFeed.getWebsite());
                serverFeed.setDescription(clientFeed.getWebsite());
                serverFeed.setTitle(clientFeed.getTitle());
            }
        }

        return serverFeed;
    }

    public static Tile keepTransientTileData (Tile serverTile) {
        if (serverTile != null) {
            Tile clientTile = (Tile)
                    DAOHelper.getTileDAO().queryTileForName(serverTile.getName());
            if (clientTile != null) {
                serverTile.setTileID(clientTile.getTileID());
                serverTile.setDrawableId(clientTile.getDrawableId());
                serverTile.setScreen(clientTile.getScreen());
            }
        }
        return serverTile;
    }
}
