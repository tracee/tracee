package de.holisticon.util.tracee.contextlogger.presets;

import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Tobias Gindler, holisticon AG on 07.02.14.
 */
public class PresetTest {

    @Test
    public void testSystemPropertyPreset() {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET, Preset.CUSTOM.name());
        Preset.reload();

        assertThat(Preset.useCustomPreset(), is(true));
    }

    @Test
    public void testSystemPropertyPresetNonCustom() {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET, Preset.BASIC.name());
        Preset.reload();

        assertThat(Preset.useCustomPreset(), is(false));
    }

    @Test
    public void testSystemPropertyPresetClass() {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET_CLASS, TestPreset.class.getCanonicalName());
        Preset.reload();

        assertThat(Preset.getCustomClassName(), is(TestPreset.class.getCanonicalName()));
    }

    @Test
    public void testFallbackCustomPresetWithEmptyClass() {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET, Preset.CUSTOM.name());
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET_CLASS, "");
        Preset.reload();

        Preset preset = Preset.getPreset();
        assertThat(preset, is(Preset.CUSTOM));

        CustomPreset customPreset = (CustomPreset) preset.getPresetConfig();

        assertThat(customPreset.getWrappedPresetConfig(), Matchers.instanceOf(BasicPreset.class));

    }

    @Test
    public void testFallbackCustomPresetWithNonexistingClass() {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET, Preset.CUSTOM.name());
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET_CLASS, "abc");
        Preset.reload();

        Preset preset = Preset.getPreset();
        assertThat(preset, is(Preset.CUSTOM));

        CustomPreset customPreset = (CustomPreset) preset.getPresetConfig();

        assertThat(customPreset.getWrappedPresetConfig(), Matchers.instanceOf(BasicPreset.class));

    }

    @Test
    public void testCustomPresetWithExistingClass() {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET, Preset.CUSTOM.name());
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET_CLASS, TestPreset.class.getCanonicalName());
        Preset.reload();

        Preset preset = Preset.getPreset();

        assertThat(preset, is(Preset.CUSTOM));

        CustomPreset customPreset = (CustomPreset) preset.getPresetConfig();

        assertThat(customPreset.getWrappedPresetConfig(), Matchers.instanceOf(TestPreset.class));

    }

    @Test
         public void testBasicPrese() {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET, Preset.BASIC.name());
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET_CLASS, "");
        Preset.reload();

        Preset preset = Preset.getPreset();

        assertThat(preset, is(Preset.BASIC));

        assertThat(preset.getPresetConfig(), Matchers.instanceOf(BasicPreset.class));

    }

    @Test
    public void testEnhancredPrese() {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET, Preset.ENHANCED.name());
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET_CLASS, "");
        Preset.reload();

        Preset preset = Preset.getPreset();

        assertThat(preset, is(Preset.ENHANCED));

        assertThat(preset.getPresetConfig(), Matchers.instanceOf(EnhancedPreset.class));

    }

    @Test
    public void testFullPrese() {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET, Preset.FULL.name());
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET_CLASS, "");
        Preset.reload();

        Preset preset = Preset.getPreset();

        assertThat(preset, is(Preset.FULL));


        assertThat(preset.getPresetConfig(), Matchers.instanceOf(FullPreset.class));

    }

}
