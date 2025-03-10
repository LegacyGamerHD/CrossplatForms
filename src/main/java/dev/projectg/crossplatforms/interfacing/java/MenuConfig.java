package dev.projectg.crossplatforms.interfacing.java;

import dev.projectg.crossplatforms.interfacing.InterfaceConfig;
import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Collections;
import java.util.Map;

@Getter
@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class MenuConfig extends InterfaceConfig {

    private transient final int defaultVersion = 1;

    private Map<String, JavaMenu> menus = Collections.emptyMap();
}
