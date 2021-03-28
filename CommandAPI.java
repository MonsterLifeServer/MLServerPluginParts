package xyz.mlserver.aooni.aooni.Utils.API;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandAPI {

    private static int COMMAND_LINES = 7;

    private static List<String> getHelps(List<String> list, int i) {
        int max;
        List<String> _list = new ArrayList<>();
        if ((list.size() % COMMAND_LINES) > 0) {
            max = (list.size() / COMMAND_LINES) + 1;
        } else {
            max = (list.size() / COMMAND_LINES);
        }
        int ListI;
        if (max < i) {
            ListI = 0;
            i = 1;
        } else {
            ListI = i - 1;
        }
        ListI = ListI * COMMAND_LINES;
        _list.add("-------- HELP(" + i + "/" + max + ") --------");
        for (int n = 0; n < COMMAND_LINES; n++) {
            if (ListI >= list.size()) return _list;
            _list.add(list.get(ListI));
            ListI++;
        }
        return _list;
    }

    public static void sendHelp(CommandSender sender, List<String> list, int i) {
        for (String text : getHelps(list, i)) {
            sender.sendMessage(text);
        }
    }

}
