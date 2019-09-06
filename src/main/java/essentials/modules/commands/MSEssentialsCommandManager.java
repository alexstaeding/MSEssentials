package essentials.modules.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

public class MSEssentialsCommandManager implements CommandManager {

    @Inject
    private SendGoogleCommand sendGoogleCommand;

    @Inject
    GoogleCommand googleCommand;

    @Inject
    StaffChatCommand staffChatCommand;

    @Inject
    NickNameCommand nickNameCommand;

    @Inject
    StaffList staffList;

    @Inject
    LanguageCommand languageCommand;

    @Inject
    DeleteNicknameCommand deleteNicknameCommand;

    @Inject
    KickCommand kickCommand;

    @Inject
    ProxyServer proxyServer;

    @Inject
    Logger logger;

    @Override
    public void register() {
        logger.info("loading commands");
        proxyServer.getCommandManager().register(sendGoogleCommand,"sendgoogle");
        proxyServer.getCommandManager().register(googleCommand, "google");
        proxyServer.getCommandManager().register(staffChatCommand, "staffchat", "sc");
        //server.getCommandManager().register(new MessageCommand(), "msg", "message", "pm");
        proxyServer.getCommandManager().register(nickNameCommand, "nick", "nickname");
        proxyServer.getCommandManager().register(staffList, "stafflist");
        proxyServer.getCommandManager().register(languageCommand, "mslang", "lang", "language");
        proxyServer.getCommandManager().register(deleteNicknameCommand, "deletenick", "delnick", "nickdel", "nickdelete");
        proxyServer.getCommandManager().register(kickCommand, "kick");

    }
}
