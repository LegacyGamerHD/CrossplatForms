package dev.projectg.crossplatforms.command;

import com.google.common.collect.ImmutableMap;
import dev.projectg.crossplatforms.Logger;
import dev.projectg.crossplatforms.handler.BedrockHandler;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public interface CommandOrigin {

    /**
     * Mapping of Logger levels to Bukkit chat colours
     */
    Map<Logger.Level, ChatColor> LOGGER_COLORS = ImmutableMap.of(
            Logger.Level.INFO, ChatColor.RESET,
            Logger.Level.WARN, ChatColor.GOLD,
            Logger.Level.SEVERE, ChatColor.RED);

    /**
     * Check the command origin has a permission
     * @param permission the permission to check
     * @return True if the command origin has the permission
     */
    boolean hasPermission(String permission);

    /**
     * Send a raw message without any plugin name prefix
     * @param message The message to send
     */
    void sendRaw(String message);

    /**
     * Send a message with the plugin name prefix
     * @param message The message to send
     */
    void sendMessage(String message);

    /**
     * @return True if the command origin represents a player
     */
    boolean isPlayer();

    /**
     * @return True if the command origin represents the console. This is not guaranteed to be the inverse of {@link CommandOrigin#isPlayer()}.
     * For example, the CommandOrigin may be a command block, which is not a Player or Console
     */
    boolean isConsole();

    /**
     * @return The underlying implementation handle
     */
    Object getHandle();

    /**
     * Is only guaranteed to return an Optional with a present value if the CommandOrigin represents a Player
     */
    Optional<UUID> getUUID();

    /**
     * Send a message with the plugin prefix, and colour.
     * @param level The Logger level (mapped to a colour if the origin is a player)
     * @param message The message to send
     */
    default void sendMessage(@Nonnull Logger.Level level, @Nonnull String message) {
        Objects.requireNonNull(level);
        Objects.requireNonNull(message);

        if (isConsole()) {
            Logger.getLogger().log(level, message);
        } else {
            // todo: abstract chat colour
            sendMessage(LOGGER_COLORS.getOrDefault(level, ChatColor.RESET) + message);
        }
    }

    /**
     * Checks if this CommandOrigin is a bedrock player through the given {@link BedrockHandler}
     * @param handler the BedrockHandler to perform the lookup with
     * @return True if the command origin is a bedrock player. false if it is anything else, such as a java player or console command sender.
     */
    default boolean isBedrockPlayer(BedrockHandler handler) {
        return isPlayer() && handler.isBedrockPlayer(getUUID().orElseThrow());
    }
}
