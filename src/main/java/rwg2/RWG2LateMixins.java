package rwg2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

@LateMixin
public final class RWG2LateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.rwg2.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<String>();
        if (loadedMods.contains("BiomesOPlenty")) mixins.add("MixinBOPSacredOak");
        if (loadedMods.contains("Natura")) mixins.add("MixinNaturaRedwoodTreeGen");
        if (loadedMods.contains("witchery")) mixins.add("MixinWitcheryComponent");
        return mixins;
    }
}
