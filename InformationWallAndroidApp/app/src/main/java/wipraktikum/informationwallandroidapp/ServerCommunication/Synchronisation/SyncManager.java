package wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation;

/**
 * Created by Eric Schmidt on 28.11.2015.
 */
public class SyncManager {
    private boolean syncFinishedContact = false;
    private boolean syncFinishedBlackboardItem = false;

    private OnSyncFinishedListener mOnSyncFinishedListener = null;

    public static SyncContact getSyncContact(){
        return SyncContact.getInstance();
    }

    public static SyncBlackboardItem getSyncBlackboardItem(){
        return SyncBlackboardItem.getInstance();
    }

    public void syncAll(){
        getSyncBlackboardItem().syncBlackBoardItems();
        getSyncContact().syncContacts();

        getSyncBlackboardItem().setOnSyncFinishedListener(new SyncBlackboardItem.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedBlackboardItem = true;
                if (syncFinishedContact == true){
                    informListener();
                }
            }
        });

        getSyncContact().setOnSyncFinishedListener(new SyncContact.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                syncFinishedContact = true;
                if (syncFinishedBlackboardItem == true){
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

    //Listener for OnActivityResult Event
    public void setOnSyncFinishedListener(OnSyncFinishedListener onSyncFinishedListener){
        mOnSyncFinishedListener = onSyncFinishedListener;
    }

    public interface OnSyncFinishedListener{
        void onSyncFinished();
    }
}
