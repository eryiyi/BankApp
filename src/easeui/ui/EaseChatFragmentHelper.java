package easeui.ui;

import android.view.View;
import com.hyphenate.chat.EMMessage;
import easeui.widget.chatrow.EaseCustomChatRowProvider;

/**
 * Created by zhl on 2016/9/24.
 */
public interface EaseChatFragmentHelper {
    /**
     * set message attribute
     */
    void onSetMessageAttributes(EMMessage message);

    /**
     * enter to chat detail
     */
    void onEnterToChatDetails();

    /**
     * on avatar clicked
     * @param username
     */
    void onAvatarClick(String username);

    /**
     * on avatar long pressed
     * @param username
     */
    void onAvatarLongClick(String username);

    /**
     * on message bubble clicked
     */
    boolean onMessageBubbleClick(EMMessage message);

    /**
     * on message bubble long pressed
     */
    void onMessageBubbleLongClick(EMMessage message);

    /**
     * on extend menu item clicked, return true if you want to override
     * @param view
     * @param itemId
     * @return
     */
    boolean onExtendMenuItemClick(int itemId, View view);

    /**
     * on set custom chat row provider
     * @return
     */
    EaseCustomChatRowProvider onSetCustomChatRowProvider();
}
