package carpet_client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;

import java.util.List;

public class RenderHelper
{
    public static void drawGuiInfoBox(TextRenderer font, String text, int startY, int infoBoxWidth, int guiWidth, int guiHeight, int bottomPadding)
    {
        if (text == null)
        {
            return;
        }
        
        int infoX = guiWidth / 2 - infoBoxWidth / 2;
        
        List<String> lines = wordWrap(text, infoBoxWidth - 10);
        int infoBoxHeight = lines.size() * font.fontHeight + 10;
        
        int infoY = startY + infoBoxHeight < guiHeight - bottomPadding ? startY : guiHeight - infoBoxHeight - bottomPadding;
        
        int y = infoY + 5;
        
        DrawableHelper.fill(infoX, infoY, infoX + infoBoxWidth, infoY + infoBoxHeight, 0xCF000000);
        
        for (String s : lines)
        {
            font.draw(s, infoX + 5, y, 0xFFFFFF);
            y += font.fontHeight;
        }
    }
    
    public static List<String> wordWrap(String s, int width)
    {
        s = s.replace("\r", ""); // If we got a \r\n in the text somehow, remove it.
        
        List<String> lines = MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(s, width);
        
        return lines;
    }
}
