package nl.enjarai.showmeyourskin.gui.widget;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import nl.enjarai.showmeyourskin.ShowMeYourSkin;
import nl.enjarai.showmeyourskin.client.DummyClientPlayerEntity;
import nl.enjarai.showmeyourskin.config.ArmorConfig;
import nl.enjarai.showmeyourskin.config.ModConfig;

import java.util.List;
import java.util.function.Supplier;

public class ConfigEntryWidget extends AbstractParentElement implements Drawable, Selectable {
    protected static final int WHITE = 0xEEEEEEEE;
    protected static final int GRAY = 0xCCCCCCCC;
    protected static final Identifier SELECTION_TEXTURE = ShowMeYourSkin.id("textures/gui/selection.png");

    public int x;
    public int y;
    public final MinecraftClient client;
    public final PlayerSelectorWidget parent;
    private final Text name;
    public final Supplier<Identifier> texture;
    protected ArmorConfig armorConfig;
    public boolean selected = false;

    public ConfigEntryWidget(MinecraftClient client, PlayerSelectorWidget parent, int x, int y, Text name, Supplier<Identifier> texture) {
        this.client = client;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.name = name;
        this.texture = texture;
        this.armorConfig = ModConfig.INSTANCE.global;
    }

    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return 30;
    }

    public int getHeight() {
        return 30;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && (x < 0 || y < 0 || isMouseOver(mouseX, mouseY))) {
            if (!super.mouseClicked(mouseX, mouseY, button)) {
                playDownSound(client.getSoundManager());
                parent.setSelected(this);
            }
            return true;
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        directRender(matrices, -1, x, y, mouseX, mouseY, isMouseOver(mouseX, mouseY), delta);
    }

    public void directRender(MatrixStack matrices, int index, int x, int y, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        RenderSystem.enableBlend();

        RenderSystem.setShaderTexture(0, SELECTION_TEXTURE);
        if (selected) {
            drawTexture(matrices, x - 1, y - 1, 0, 0, 32, 32, 128, 128);
        } else if (hovered && children().stream().noneMatch(element -> element.isMouseOver(mouseX, mouseY))) {
            drawTexture(matrices, x - 1, y - 1, 32, 0, 32, 32, 128, 128);
        }

        renderIcon(matrices, index, x, y, mouseX, mouseY, hovered, tickDelta);

        RenderSystem.disableBlend();
    }

    protected void renderIcon(MatrixStack matrices, int index, int x, int y, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        RenderSystem.setShaderTexture(0, texture.get());
        drawTexture(matrices, x + 3, y + 3, 24, 24, 0, 0, 24, 24, 24, 24);
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 0.4F));
    }

    @Override
    public List<? extends Element> children() {
        return ImmutableList.of();
    }

    public ArmorConfig getArmorConfig() {
        return armorConfig;
    }

    public PlayerEntity getDummyPlayer() {
        return DummyClientPlayerEntity.getInstance();
    }

    public Text getName() {
        return name;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX < x + getWidth() && mouseY >= y && mouseY < y + getHeight();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }
}
