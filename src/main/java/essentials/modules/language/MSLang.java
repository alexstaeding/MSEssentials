package essentials.modules.language;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.velocitypowered.api.command.CommandSource;
import essentials.modules.Config.ConfigKeys;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

public class MSLang {

    @Inject
    @Named("msessentials")
    ConfigurationService configurationService;

    public boolean addSwear(String swear, CommandSource src) {
        List<String> swears = new ArrayList<>(configurationService.getConfigList(ConfigKeys.SWEARS_LIST, new TypeToken<List<String>>() {
        }));
        if (swears.stream().noneMatch(s -> s.equalsIgnoreCase(swear))) {
            src.sendMessage(TextComponent.of("swears doesnt contain the swear"));
            if (!swears.isEmpty()) {
                src.sendMessage(TextComponent.of("swears isn't empty"));
//                for (String s : swears)
//                {
//                    if (swear.equalsIgnoreCase(s))
//                    {
//                        return false;
//                    }
//                }
            }
            swears.add(swear);
            src.sendMessage(TextComponent.of(swears.toString()));
            swears.replaceAll(s -> s.replaceAll("[\\[\\]]", ""));
            configurationService.setConfigList(ConfigKeys.SWEARS_LIST, swears);
            configurationService.save();
            return true;
        }
        return false;
    }

    public boolean addExempt(String exempt, CommandSource src) {
        List<String> exceptions = configurationService.getConfigList(ConfigKeys.SWEARS_EXCEPTION_LIST, new TypeToken<List<String>>() {
        });
        if (!exceptions.contains(exempt)) {
            if (!exceptions.isEmpty()) {
                for (String s : exceptions) {
                    if (exempt.equalsIgnoreCase(s)) {
                        return false;
                    }
                }
            }
            configurationService.addToConfigList(ConfigKeys.SWEARS_EXCEPTION_LIST, exempt, new TypeToken<List<String>>() {
            });
            configurationService.save();
            return true;
        }
        return false;
    }

    public boolean removeSwear(String swear, CommandSource src) {
        List<String> swears = configurationService.getConfigList(ConfigKeys.SWEARS_LIST, new TypeToken<List<String>>() {
        });
        if (!swears.isEmpty()) {
            for (String s : swears) {
                if (swear.equalsIgnoreCase(s)) {
                    configurationService.removeFromConfigList(ConfigKeys.SWEARS_LIST, swear);
                    configurationService.save();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeExempt(String e, CommandSource src) {
        List<String> exceptions = configurationService.getConfigList(ConfigKeys.SWEARS_EXCEPTION_LIST, new TypeToken<List<String>>() {
        });
        if (!exceptions.isEmpty()) {
            for (String s : exceptions) {
                if (e.equalsIgnoreCase(s)) {
                    configurationService.removeFromConfigList(ConfigKeys.SWEARS_EXCEPTION_LIST, e);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSwearsEmpty() {
        return configurationService.getConfigList(ConfigKeys.SWEARS_LIST).isEmpty();
    }

    public List<String> getSwears() {
        return configurationService.getConfigList(ConfigKeys.SWEARS_LIST, new TypeToken<List<String>>() {
        });
    }

    public boolean isExceptionsEmpty() {
        return configurationService.getConfigList(ConfigKeys.SWEARS_EXCEPTION_LIST).isEmpty();
    }

    public List<String> getExceptions() {
        return configurationService.getConfigList(ConfigKeys.SWEARS_EXCEPTION_LIST, new TypeToken<List<String>>() {
        });
    }
}
