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

    public static SyncContact getSyncContact(){
        return SyncContact.getInstance();
    }

    public static SyncBlackboardItem getSyncBlackboardItem(){
        return SyncBlackboardItem.getInstance();
    }

    public static SyncTile getSyncTile(){
        return  SyncTile.getInstance();
    }

    public static SyncFeed getSyncFeed(){
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
                if (syncFinished()) {
                    informListener();
                }
            }
        });

        getSyncContact().setOnSyncFinishedListener(new SyncContact.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedContact = true;
                if (syncFinished()){
                    informListener();
                }
            }
        });

        getSyncTile().setOnSyncFinishedListener(new SyncTile.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedTile = true;
                if (syncFinished()){
                    informListener();
                }
            }
        });

        getSyncFeed().setOnSyncFinishedListener(new SyncFeed.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedFeed = true;
                if (syncFinished()){
                    informListener();
                }
            }
        });
    }

    private void informListener(){
        if (mOnSyncFinishedListener != null){
            mOnSyncFinishedListener.onSyncFinished();
        }
    }

    private boolean syncFinished(){
        if (syncFinishedContact && syncFinishedBlackboardItem && syncFinishedTile) return true;
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
