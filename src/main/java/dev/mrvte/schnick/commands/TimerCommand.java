package dev.mrvte.schnick.commands;


import dev.mrvte.schnick.Schnick;
import dev.mrvte.schnick.timer.Timer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TimerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0){
          sendUsage(sender);
            return  true;
        }
        switch (args[0].toLowerCase()){
            case "resume": {
                Timer timer = Schnick.getInstance().getTimer();

                if (timer.isRunning()) {
                    sender.sendMessage(ChatColor.RED + "Der Timer läuft bereits!");
                }
                timer.setRunning(true);
                sender.sendMessage(ChatColor.GRAY + "Der Timer wurde gestartet!");
                break;
            }
            case "pause": {
                Timer timer = Schnick.getInstance().getTimer();

                if (timer.isRunning()) {
                    sender.sendMessage(ChatColor.RED + "Der Timer läuft nicht!");
                }
                timer.setRunning(false);
                sender.sendMessage(ChatColor.GRAY + "Der Timer wurde gestoppt!");
                break;
            }
            case "time":{
                //Timer timer = Main.getInstance().getTimer();
                if(args.length != 2){
                    sender.sendMessage("§7Verwengun§8: §9/timer time <Zeit>");
                    return  true;
                }

                try {
                    Timer timer = Schnick.instance.getTimer();
                    timer.setRunning(false);
                    timer.setTime(Integer.parseInt(args[1]));
                    sender.sendMessage(ChatColor.GRAY + "Der Timer wurde auf " +args[1] + " gesetzt!");

                }catch (NumberFormatException e){
                    sender.sendMessage(ChatColor.RED + "Dein Parameter 2 muss eine Zahl sein!");
                }


                break;
            }
            case "reset": {
                Timer timer = Schnick.getInstance().getTimer();


                timer.setRunning(false);
                timer.setTime(0);
                sender.sendMessage(ChatColor.GRAY + "Der Timer wurde zurückgesetzt!");
                break;
            }
            default:
                sendUsage(sender);
                break;

        }
        return false;

    }
    private void sendUsage(CommandSender sender ){
        sender.sendMessage("§7Verwendung§8: §9/timer resume, /timer pause, /timer time <Zeit>, /timer reset");
    }
}
