package me.whitehatd.Currencies.Commands.Command;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import me.whitehatd.Currencies.Commands.CommandBase;
import me.whitehatd.Currencies.Currencies;
import me.whitehatd.Currencies.Utilities.ChatUtil;
import me.whitehatd.Currencies.Utilities.Config.Config;
import me.whitehatd.Currencies.Utilities.Config.ConfigUtil;
import me.whitehatd.Currencies.Utilities.CurrencyUtil;
import me.whitehatd.Currencies.Utilities.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EconomyNPC extends CommandBase {

    public EconomyNPC(){
        super("econpc", true, "currencies.shop");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        switch (args.length){
            case 2: {
                if(!args[0].equalsIgnoreCase("shop")){
                    ChatUtil.cs(player, "econpc.usage");
                    return;
                }
                if(!CurrencyUtil.getCurrencies().contains(args[1].toLowerCase())){
                    ChatUtil.cs(sender, "invalid-currency");
                    return;
                }
                String currency = args[1].toLowerCase();
                String path = "currencies." + currency + ".shop";
                SGMenu menu = Currencies.spiGUI.create(ConfigUtil.str(path + ".title"),
                                                        ConfigUtil.num(path + ".size")/9-1);
                int x = 10;
                menu.setOnPageChange(m -> m.setAutomaticPaginationEnabled(true));
                for(String s : Utils.getConfig().getConfigurationSection(path + ".contents").getKeys(false)) {
                    String itemPath = path + ".contents." + s;
                    int price = ConfigUtil.num(itemPath + ".price");
                    ArrayList<String> lore = ConfigUtil.arr(itemPath + ".lore");
                    lore.replaceAll(t -> t.replaceAll("%price%", price+"")
                                            .replaceAll("%sign%", CurrencyUtil.getCurrencySign(currency)));
                    ItemStack itm = new ItemBuilder(Material.valueOf(ConfigUtil.str(itemPath + ".type")))
                            .name(ConfigUtil.str(itemPath + ".name"))
                            .amount(ConfigUtil.num(itemPath + ".amount"))
                            .lore(lore).build();
                    SGButton button = new SGButton(itm
                    ).withListener((InventoryClickEvent e) -> {
                        String uuid = e.getWhoClicked().getUniqueId().toString();
                        Player p = (Player) e.getWhoClicked();
                        if(CurrencyUtil.getBalance(uuid, currency) < price){
                            ChatUtil.cs(p, "shop.no-money");
                        }else{
                            if(p.getInventory().firstEmpty()==-1){
                                ChatUtil.cs(p, "shop.no-space");
                            }else{
                                CurrencyUtil.setBalance(uuid, currency, CurrencyUtil.getBalance(uuid, currency) - price);
                                p.getInventory().addItem(itm);
                                ChatUtil.cs(p, "shop.success", t -> t.replaceAll("%item%", ConfigUtil.str(itemPath + ".name"))
                                                                            .replaceAll("%price%", price+"")
                                                                            .replaceAll("%sign%", CurrencyUtil.getCurrencySign(currency)));
                            }
                        }

                    });
                    menu.setButton(x, button);
                    x++;
                    if ((x + 1) % 9 == 0) x = x + 2;
                    if (x / 9 == 4) x = x + 18;
                }
                for(int i=0;i<menu.getRowsPerPage();i++){
                    SGButton b = new SGButton(new ItemStack(Material.valueOf(ConfigUtil.str(path+".filler"))));
                    menu.setButton(i * 9, b);
                    menu.setButton(i * 9 + 8, b);
                    menu.stickSlot(i*9);
                    if(i < menu.getRowsPerPage() - 1)
                        menu.stickSlot(i*9+8);
                }
                SGButton exit = new SGButton(new ItemBuilder(Material.BARRIER).name(ChatUtil.c("&c&lExit")).build()).withListener(
                        ((InventoryClickEvent e) -> {
                            player.closeInventory();
                        }
                ));
                menu.setButton(menu.getPageSize()-1, exit);
                for(int i=1;i<8;i++){
                    SGButton b = new SGButton(new ItemStack(Material.valueOf(ConfigUtil.str(path+".filler"))));
                    menu.setButton(i, b);
                    menu.setButton(menu.getPageSize() - i -1, b);
                    menu.stickSlot(i);
                    menu.stickSlot(menu.getPageSize() - i -1);
                }
                player.openInventory(menu.getInventory());
                return;
            }
            default: {
                ChatUtil.cs(player, "econpc.usage");
                return;
            }
        }
    }

    @Override
    public List<String> tab(CommandSender sender, Command cmd, String[] args) {
        if(args.length == 2){
            return CurrencyUtil.getCurrencies()
                    .stream()
                    .filter(def -> def.startsWith(args[1]))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
