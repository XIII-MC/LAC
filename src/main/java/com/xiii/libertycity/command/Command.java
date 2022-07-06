package com.xiii.libertycity.command;

import com.xiii.libertycity.data.Data;
import com.xiii.libertycity.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("lac")) {
            if (sender.hasPermission("LibertyCity.LAC.command.help")) {
                if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage("§8§M+--------------------------------------------------+");
                    sender.sendMessage("");
                    sender.sendMessage("§f/lac §7(help) §8| §fMontrer ce message.");
                    sender.sendMessage("§f/lac §6alerts §8| §fActivé/Désactivé les alertes.");
                    sender.sendMessage("§f/lac §6kick §e<Joueur> §8| §fExplusé un joueur pour 'Triche'.");
                    sender.sendMessage("§f/lac §6ban §e<Joueur> §8| §fBanniser un joueur pour 'Triche'.");
                    sender.sendMessage("");
                    sender.sendMessage("§8§M+--------------------------------------------------+");
                } else if (args.length == 1 && args[0].equalsIgnoreCase("alerts")) {
                    if (sender.hasPermission("LibertyCity.LAC.command.alerts")) {
                        PlayerData data = Data.data.getUserData((Player) sender);
                        if (data.alertstoggled) {
                            data.alertstoggled = false;
                            sender.sendMessage("§2§lL§A§LAC §8»§f §fAlertes §a§nactivées§r §f!");
                        } else {
                            data.alertstoggled = true;
                            sender.sendMessage("§2§lL§A§LAC §8»§f §fAlertes §c§ndésactivées§r §f!");
                        }
                        return true;
                    } else sender.sendMessage("§2§lL§A§LAC §8»»§c Permission Insuffisante.'");
                } else if (args.length == 2 && args[0].equalsIgnoreCase("kick")) {
                    if (sender.hasPermission("LibertyCity.LAC.command.kick")) {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (target.isOnline()) {
                            target.kickPlayer("§7§m---------------§2§lL§A§LAC§7§M---------------" + "\n" + "§cVous avez été explusé(e) du serveur!" + "\n" + " " + "\n" + "§2§lL§A§LAC §8» §cCHEAT DETECTION (Triche) " + "\n" + "§7Sanction effectuer par §2§lL§A§LAC" + "\n" + " " + "\n" + "§bPour contester cette sanction, veuillez utiliser le Discord:" + "\n" + "§4§nSanction non-contestable." + "\n" + "§7§m---------------------------------");
                            sender.sendMessage("§2§lL§A§LAC §8» §e" + target.getName() + " §fa été explusé.");
                        } else sender.sendMessage("§2§lL§A§LAC §8»»§c Attention! Ce joueur n'est pas en ligne'");
                    } else sender.sendMessage("§2§lL§A§LAC §8»»§c Permission Insuffisante.'");
                } else if (args.length == 2 && args[0].equalsIgnoreCase("ban")) {
                    if (sender.hasPermission("LibertyCity.LAC.command.ban")) {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (target.isOnline()) {
                            target.kickPlayer("§7§m---------------§2§lL§A§LAC§7§M---------------" + "\n" + "§cVous avez été banni du serveur!" + "\n" + " " + "\n" + "§2§lL§A§LAC §8» §cCHEAT DETECTION (Triche) " + "\n" + "§7Sanction effectuer par §2§lL§A§LAC" + "\n" + " " + "\n" + "§bPour contester cette sanction, veuillez utiliser le Discord:" + "\n" + "§4§nSanction non-contestable." + "\n" + "§7§m---------------------------------");
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " + target.getName() + " perm Triche -s -ip");
                            sender.sendMessage("§2§lL§A§LAC §8» §e" + target.getName() + " §fa été banni.");
                        } else sender.sendMessage("§2§lL§A§LAC §8»»§c Attention! Ce joueur n'est pas en ligne'");
                    } else sender.sendMessage("§2§lL§A§LAC §8»»§c Permission Insuffisante.'");
                }
            }
        }
        return true;
    }
}
