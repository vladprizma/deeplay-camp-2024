package chat;

import action.Action;

public class ChatModel {

    private String chatMessages;
    private Action action;

    public ChatModel(Action action, String chatMessages) {
        this.chatMessages = chatMessages;
        this.action = action;
        chatModelMethod();
    }

    private void chatModelMethod() {
        action.handleChatAction(chatMessages);
    }
}
