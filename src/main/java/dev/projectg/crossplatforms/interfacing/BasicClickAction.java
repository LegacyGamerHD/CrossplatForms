package dev.projectg.crossplatforms.interfacing;

import dev.projectg.crossplatforms.CrossplatForms;
import dev.projectg.crossplatforms.Logger;
import dev.projectg.crossplatforms.utils.PlaceholderUtils;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ToString
@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class BasicClickAction implements ClickAction {

    @Nonnull
    protected List<String> commands = Collections.emptyList();

    @Nullable
    protected String server = null;

    @Override
    public void affectPlayer(@Nonnull InterfaceManager interfaceManager, @Nonnull Player player) {
        affectPlayer(interfaceManager, player, Collections.emptyMap());
    }

    @Override
    public void affectPlayer(@Nonnull InterfaceManager interfaceManager, @Nonnull Player player, @Nonnull Map<String, String> additionalPlaceholders) {
        // Get the commands from the list of commands and replace any playerName placeholders
        Logger logger = Logger.getLogger();
        if (!commands.isEmpty()) {
            for (String command : commands) {
                interfaceManager.runCommand(
                        PlaceholderUtils.setPlaceholders(player, command, additionalPlaceholders),
                        player.getUniqueId());
            }
        } else {
            logger.debug("No commands");
        }

        if (server == null) {
            logger.debug("No server");
        } else if (server.isBlank()) {
            logger.debug("server is empty");
        } else {
            String resolved = PlaceholderUtils.setPlaceholders(player, server, additionalPlaceholders);
            // This should never be out of bounds considering its size is the number of valid buttons
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream(); DataOutputStream out = new DataOutputStream(stream)) {
                Logger.getLogger().debug("Attempting to send " + player.getName() + " to BungeeCord server " + server);
                out.writeUTF("Connect");
                out.writeUTF(resolved);
                player.sendPluginMessage(CrossplatForms.getInstance(), "BungeeCord", stream.toByteArray());
            } catch (IOException e) {
                Logger.getLogger().severe("Failed to send a plugin message to BungeeCord!");
                e.printStackTrace();
            }
        }
    }
}
