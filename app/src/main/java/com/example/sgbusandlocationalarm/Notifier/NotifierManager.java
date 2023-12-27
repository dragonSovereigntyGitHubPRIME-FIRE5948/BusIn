package com.example.sgbusandlocationalarm.Notifier;

import com.google.firebase.firestore.Query;

public class NotifierManager {

    private static volatile NotifierManager instance;
    private NotifierRepo notifierRepo;
    private NotifierManager() {
        notifierRepo = notifierRepo.getInstance();
    }

    public static NotifierManager getInstance() {
        NotifierManager result = instance;
        if (result != null) {return result;}
        synchronized(NotifierManager.class) {
            if (instance == null) {instance = new NotifierManager();}
            return instance;}
    }

    public void createNotifier(NotifierModel notifier) {
        notifierRepo.createNotifier(notifier);
    }

//    /** Retrieves chat rooms of current user. */
//    // func runs only if user is logged in (getCurrentUser)
//    // only retrieves chat room docs that have user account as a member
//    public Query queryAllChatRooms() {
//        return chatRoomRepo.queryAllChatRooms();
//    }
//
//    /** Retrieve a chat room with the chat room uid.
//     Used for 1to1 chat rooms.
//     Chat room uid is a composite uid of both accounts.
//     * */
//    public Task<ChatRoomModel> getChatRoom(String chatRoomUid) {
//        return chatRoomRepo.getChatRoom(chatRoomUid).continueWith(task ->
//                task.getResult().toObject(ChatRoomModel.class));
//    }
//
//    /** Retrieves all messages (limit 50) of a chat room. */
//    public Query queryAllMessages(String chatRoomUid) {return chatRoomRepo.queryAllMessages(chatRoomUid);}

    public Query queryAllNotifiers() {
        return notifierRepo.queryAllNotifiers();
    }

}
