package net.avdw.color.generator;

import net.avdw.color.ColorConverter;
import net.avdw.color.RGB;
import net.avdw.number.generator.NumberGenerator;

public class HSVColorGenerator implements ColorGenerator {
    private final NumberGenerator hueGenerator;
    private final NumberGenerator saturationGenerator;
    private final NumberGenerator valueGenerator;
    private final ColorConverter colorConverter;

    HSVColorGenerator(final NumberGenerator hueGenerator, final NumberGenerator saturationGenerator, final NumberGenerator valueGenerator, final ColorConverter colorConverter) {
        this.hueGenerator = hueGenerator;
        this.saturationGenerator = saturationGenerator;
        this.valueGenerator = valueGenerator;
        this.colorConverter = colorConverter;
    }

    /**
     * Generate a color in the RGB format.
     * R in range [0..1]
     * G in range [0..1]
     * B in range [0..1]
     *
     * @return color in RGB
     */
    @Override
    public RGB generateRGB() {
        int hue = (int) Math.max(0, Math.min(360, hueGenerator.nextValue()));
        double saturation = Math.max(0, Math.min(1, saturationGenerator.nextValue()));
        double value = Math.max(0, Math.min(1, valueGenerator.nextValue()));
        return colorConverter.hsvToRgb(hue, saturation, value);
    }
}
