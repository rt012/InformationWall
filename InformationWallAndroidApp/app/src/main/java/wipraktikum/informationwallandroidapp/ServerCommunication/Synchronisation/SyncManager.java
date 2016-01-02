package wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation;

/**
 * Created by Eric Schmidt on 28.11.2015.
 */
public class SyncManager {
    private boolean syncFinishedContact = false;
    private boolean syncFinishedBlackboardItem = false;
    private boolean syncFinishedTile = false;
    private boolean syncFinishedFeed = false;

    private OnSyncFinishedListener mOnSyncFinishedListener = null;

    private static SyncContact getSyncContact(){
        return SyncContact.getInstance();
    }

    private static SyncBlackboardItem getSyncBlackboardItem(){
        return SyncBlackboardItem.getInstance();
    }

    private static SyncTile getSyncTile(){
        return  SyncTile.getInstance();
    }

    private static SyncFeed getSyncFeed(){
        return  SyncFeed.getInstance();
    }

    public void syncAll(){
        getSyncBlackboardItem().syncBlackBoardItems();
        getSyncContact().syncContacts();
        getSyncTile().syncTiles();
        getSyncFeed().syncFeeds();

        getSyncBlackboardItem().setOnSyncFinishedListener(new SyncBlackboardItem.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedBlackboardItem = true;
                if (allSyncsFinished()) {
                    informListener();
                }
            }
        });

        getSyncContact().setOnSyncFinishedListener(new SyncContact.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedContact = true;
                if (allSyncsFinished()){
                    informListener();
                }
            }
        });

        getSyncTile().setOnSyncFinishedListener(new SyncTile.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedTile = true;
                if (allSyncsFinished()){
                    informListener();
                }
            }
        });

        getSyncFeed().setOnSyncFinishedListener(new SyncFeed.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedFeed = true;
                if (allSyncsFinished()){
                    informListener();
                }
            }
        });
    }

    public void syncBlackboardInformation(){
        getSyncBlackboardItem().syncBlackBoardItems();
        getSyncContact().syncContacts();

        getSyncBlackboardItem().setOnSyncFinishedListener(new SyncBlackboardItem.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedBlackboardItem = true;
                if (syncFinishedContact) {
                    informListener();
                }
            }
        });

        getSyncContact().setOnSyncFinishedListener(new SyncContact.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedContact = true;
                if (syncFinishedBlackboardItem){
                    informListener();
                }
            }
        });
    }

    public void syncFeedReaderInformation(){
        getSyncFeed().syncFeeds();

        getSyncFeed().setOnSyncFinishedListener(new SyncFeed.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                informListener();
            }
        });
    }

    public void syncTileInformation(){
        getSyncTile().syncTiles();

        getSyncTile().setOnSyncFinishedListener(new SyncTile.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                informListener();
            }
        });

    }

    private void informListener(){
        if (mOnSyncFinishedListener != null){
            mOnSyncFinishedListener.onSyncFinished();
        }
    }

    private boolean allSyncsFinished(){
        if (syncFinishedContact && syncFinishedBlackboardItem && syncFinishedTile && syncFinishedFeed) return true;
        return false;
    }

    //Listener for OnActivityResult Event
    public void setOnSyncFinishedListener(OnSyncFinishedListener onSyncFinishedListener){
        mOnSyncFinishedListener = onSyncFinishedListener;
    }

    public interface OnSyncFinishedListener{
        void onSyncFinished();
    }
}
