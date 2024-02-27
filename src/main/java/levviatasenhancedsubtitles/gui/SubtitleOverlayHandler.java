package levviatasenhancedsubtitles.gui;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import levviatasenhancedsubtitles.config.LESConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISoundEventListener;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SubtitleOverlayHandler
{

    /*private final Minecraft minecraft = Minecraft.getMinecraft();
    static final List<Subtitle> subtitles = Lists.newArrayList();
    private boolean isListening;

    public static void clientPreInit()
    {
        MinecraftForge.EVENT_BUS.register(new SubtitleOverlayHandler());
    }
    public static void preInit()
    {

    }private boolean isDragging = false;
    private int dragX = 0;
    private int dragY = 0;
    private int lastMouseX = 0;
    private int lastMouseY = 0;

    // ... existing code ...

    // Call this method when the mouse is clicked
    /*@SubscribeEvent
    public static void onMouseEvent(MouseEvent event) {
        SubtitleOverlayHandler handler = new SubtitleOverlayHandler();
        if (event.getButton() == 0 && event.isButtonstate()) {
            handler.mouseClicked(event.getX(), event.getY(), event.getButton());
        } else if (event.getButton() == 0 && !event.isButtonstate()) {
            handler.mouseReleased(event.getX(), event.getY(), event.getButton());
        }
        if (event.getDx() != 0 || event.getDy() != 0) {
            handler.mouseClickMove(event.getX(), event.getY(), event.getButton(), event.getNanoseconds());
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        // Check if it's the subtitles overlay being rendered
        if (event.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES) {

            SubtitleOverlayHandler handler = new SubtitleOverlayHandler();
            event.setCanceled(true);
            handler.render(event.getResolution());

        }
    }
    public void render(ScaledResolution resolution)
    {
        if (!this.isListening && minecraft.gameSettings.showSubtitles)
        {
            minecraft.getSoundHandler().addListener(this);
            this.isListening = true;
        }
        else if (this.isListening && !minecraft.gameSettings.showSubtitles)
        {
            minecraft.getSoundHandler().removeListener(this);
            this.isListening = false;
        }

        if (this.isListening && !subtitles.isEmpty())
        {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Vec3d playerPosition = new Vec3d(minecraft.player.posX, minecraft.player.posY + (double)minecraft.player.getEyeHeight(), minecraft.player.posZ);
            Vec3d zPlayerDirection = (new Vec3d(0.0D, 0.0D, -1.0D)).rotatePitch(-minecraft.player.rotationPitch * 0.017453292F).rotateYaw(-minecraft.player.rotationYaw * 0.017453292F);
            Vec3d yPlayerDirection = (new Vec3d(0.0D, 1.0D, 0.0D)).rotatePitch(-minecraft.player.rotationPitch * 0.017453292F).rotateYaw(-minecraft.player.rotationYaw * 0.017453292F);
            Vec3d vec3d3 = zPlayerDirection.crossProduct(yPlayerDirection);
            int maxLength = 0;
            Iterator<Subtitle> iterator = subtitles.iterator();

            while (iterator.hasNext())
            {
                Subtitle caption = iterator.next();

                if (caption.getStartTime() + 3000L <= Minecraft.getSystemTime())
                {
                    iterator.remove();
                }
                else
                {
                    maxLength = Math.max(maxLength, minecraft.fontRenderer.getStringWidth(caption.getString()));
                }
            }

            maxLength = maxLength + minecraft.fontRenderer.getStringWidth("<") + minecraft.fontRenderer.getStringWidth(" ") + minecraft.fontRenderer.getStringWidth(">") + minecraft.fontRenderer.getStringWidth(" ");

            int captionIndex = 0;
            for (Subtitle caption : subtitles)
            {
                String Caption1 = caption.getString();

                Vec3d vec3d4 = caption.getLocation().subtract(playerPosition).normalize();
                double d0 = -vec3d3.dotProduct(vec3d4);
                double d1 = -zPlayerDirection.dotProduct(vec3d4);
                boolean flag = d1 > 0.5D;

                int halfMaxLength = maxLength / 2;

                int subtitleHeight = minecraft.fontRenderer.FONT_HEIGHT;
                int subtitleWidth = minecraft.fontRenderer.getStringWidth(Caption1);

                int fadeAwayCalculation = MathHelper.floor(MathHelper.clampedLerp(255.0D, 75.0D, (float)(Minecraft.getSystemTime() - caption.getStartTime()) / 3000.0F));
                int fadeAway = fadeAwayCalculation << 16 | fadeAwayCalculation << 8 | fadeAwayCalculation;

                int red = LESConfiguration.propBackgroundRed.getInt();
                int green = LESConfiguration.propBackgroundGreen.getInt();
                int blue = LESConfiguration.propBackgroundBlue.getInt();
                int backgroundSubtitleAlphaCalculation = LESConfiguration.propBackgroundAlpha.getInt();
                int backgroundSubtitleColor = (backgroundSubtitleAlphaCalculation << 24) | (red << 16) | (green << 8) | blue;

                GlStateManager.pushMatrix();

                //Change happens here

                String position = LESConfiguration.propOverlayPosition.getString();

                int verticalSpacing = 1;
                int horizontalSpacing = 2;
                int subtitleSpacing = 10;
                float xTranslate, yTranslate;

                switch (position) {
                    case "BOTTOM_CENTER":
                        xTranslate = (float) resolution.getScaledWidth() / 2;
                        yTranslate = (float) (resolution.getScaledHeight() - 75) - (float) (captionIndex * subtitleSpacing);
                        break;
                    case "BOTTOM_LEFT":
                        xTranslate = (float) halfMaxLength + horizontalSpacing;
                        yTranslate = (float) (resolution.getScaledHeight() - 30) - (float) (captionIndex * subtitleSpacing);
                        break;
                    case "CENTER_LEFT":
                        xTranslate = (float) halfMaxLength + horizontalSpacing;
                        yTranslate = (float) (resolution.getScaledHeight() / 2) - (float) (((subtitles.size() - 1) / 2) - captionIndex) * subtitleSpacing;
                        break;
                    case "TOP_LEFT":
                        xTranslate = (float) halfMaxLength + horizontalSpacing;
                        yTranslate = (float) (captionIndex * subtitleSpacing + 5 + verticalSpacing);
                        break;
                    case "TOP_CENTER":
                        xTranslate = (float) resolution.getScaledWidth() / 2;

                        yTranslate = (float) (captionIndex * subtitleSpacing + 5 + verticalSpacing);
                        break;
                    case "TOP_RIGHT":
                        xTranslate = (float) resolution.getScaledWidth() - (float) halfMaxLength - 2;
                        yTranslate = (float) (captionIndex * subtitleSpacing + 5 + verticalSpacing);
                        break;
                    case "CENTER_RIGHT":
                        xTranslate = (float) resolution.getScaledWidth() - (float) halfMaxLength - horizontalSpacing;
                        yTranslate = (float) (resolution.getScaledHeight() / 2) - (float) (((subtitles.size() - 1) / 2) - captionIndex) * subtitleSpacing;
                        break;
                    default: //if there's any invalid input just show it in the bottom right
                        xTranslate = (float) resolution.getScaledWidth() - (float) halfMaxLength - 2;
                        yTranslate = (float) (resolution.getScaledHeight() - 30) - (float) (captionIndex * subtitleSpacing);
                        break;
                }
                xTranslate += this.dragX;
                yTranslate += this.dragY;
                GlStateManager.translate(xTranslate, yTranslate, 0.0F);

                GlStateManager.scale(1.0F, 1.0F, 1.0F);

                drawRect(-halfMaxLength - 1, -subtitleHeight / 2 - 1, halfMaxLength + 1, subtitleHeight / 2 + 1, backgroundSubtitleColor);

                GlStateManager.enableBlend();

                if (!flag) {
                    if (d0 > 0.00D)
                    {
                        minecraft.fontRenderer.drawString(">", halfMaxLength - minecraft.fontRenderer.getStringWidth(">"), -subtitleHeight / 2, fadeAway + 16777216);
                    }
                    else if (d0 < -0.00D)
                    {
                        minecraft.fontRenderer.drawString("<", -halfMaxLength, -subtitleHeight / 2, fadeAway + 16777216);
                    }

                }

                minecraft.fontRenderer.drawString(Caption1, -subtitleWidth / 2, -subtitleHeight / 2, fadeAway + 16777216);
                GlStateManager.popMatrix();
                ++captionIndex;
            }

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
    @Override
    public void soundPlay(ISound soundIn, SoundEventAccessor accessor) {
        // Your custom implementation here
        if (accessor.getSubtitle() != null) {
            String subtitleText = accessor.getSubtitle().getFormattedText();

            if (!SubtitleOverlayHandler.subtitles.isEmpty()) {
                for (Subtitle caption : SubtitleOverlayHandler.subtitles) {
                    if (caption.getString().equals(subtitleText)) {
                        caption.refresh(new Vec3d(soundIn.getXPosF(), soundIn.getYPosF(), soundIn.getZPosF()));
                        return;
                    }
                }
            }
            SubtitleOverlayHandler.subtitles.add(new Subtitle(subtitleText, new Vec3d(soundIn.getXPosF(), soundIn.getYPosF(), soundIn.getZPosF())));
        }
    }*/
    public static class Subtitle
    {
        private final String subtitle;
        private long startTime;
        private Vec3d location;
        private String text;
        private int x, y;
        private int width, height;
        private boolean isDragging;

        public Subtitle(String subtitleIn, Vec3d locationIn)
        {
            this.subtitle = subtitleIn;
            this.location = locationIn;
            this.startTime = Minecraft.getSystemTime();
        }
        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean isMouseOver(int mouseX, int mouseY) {
            return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
        }

        public String getString()
        {
            return this.subtitle;
        }

        public long getStartTime()
        {
            return this.startTime;
        }

        public Vec3d getLocation()
        {
            return this.location;
        }

        public void refresh(Vec3d locationIn)
        {
            this.location = locationIn;
            this.startTime = Minecraft.getSystemTime();
        }
    }
}
