package ru.engineers.progression.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class EngineeringArchiveScreen extends Screen {
    private static final int BRASS = 0xFFB98545;
    private static final int GOLD = 0xFFFFC15A;
    private static final int DARK = 0xFF211B15;
    private static final int PANEL = 0xFF30251B;
    private static final int MUTED = 0xFFB7A58E;
    private static final String[] TABS = {
            "Механика", "Промышленность", "Снаряжение", "Авиастроение", "Изобретения"
    };
    private static final String[][] NODES = {
            {"Основы механики", "Передача движения", "Обработка деталей", "Автоматизация", "Сложные механизмы"},
            {"Обработка сырья", "Металлургия", "Производственные линии", "Складские системы", "Индустриальный комплекс"},
            {"Рабочее снаряжение", "Инструментальное дело", "Металлическая защита", "Механические улучшения", "Высшая экипировка"},
            {"Воздушная инженерия", "Конструирование", "Двигательные системы", "Воздушный транспорт", "Воздушные комплексы"},
            {"Техническая документация", "Экспериментальные материалы", "Механические эксперименты", "Инженерные разработки", "Архитектура изобретений"}
    };

    private int selectedTab = 0;
    private int selectedNode = 0;
    private String page = "tree";

    public EngineeringArchiveScreen() {
        super(Component.literal("Инженерный архив"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        selectedTab = Math.max(0, Math.min(selectedTab, TABS.length - 1));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g, mouseX, mouseY, partialTick);
        int w = Math.min(920, this.width - 24);
        int h = Math.min(510, this.height - 24);
        int x = (this.width - w) / 2;
        int y = (this.height - h) / 2;

        g.fill(x - 5, y - 5, x + w + 5, y + h + 5, 0xFF100D0A);
        panel(g, x, y, w, h, DARK, BRASS);

        // Header
        panel(g, x + 10, y + 10, w - 20, 58, PANEL, BRASS);
        g.drawString(font, "ИНЖЕНЕРЫ", x + 27, y + 17, GOLD, false);
        g.drawString(font, "ИНЖЕНЕРНЫЙ АРХИВ", x + 28, y + 38, MUTED, false);
        g.drawString(font, "ИНЖЕНЕРНЫЙ ДОПУСК", x + w - 190, y + 18, GOLD, false);
        g.drawString(font, "II. МЕХАНИЧЕСКИЙ", x + w - 190, y + 39, 0xFFFFFFFF, false);
        g.fill(x + w - 190, y + 54, x + w - 25, y + 59, 0xFF57422C);
        g.fill(x + w - 190, y + 54, x + w - 122, y + 59, GOLD);

        // Tabs
        int tabY = y + 76;
        int tabW = (w - 28) / 5;
        for (int i = 0; i < TABS.length; i++) {
            int tx = x + 10 + i * tabW;
            boolean active = selectedTab == i && page.equals("tree");
            panel(g, tx, tabY, tabW - 4, 30, active ? 0xFF5A3D1F : PANEL, active ? GOLD : BRASS);
            centered(g, TABS[i], tx + (tabW - 4) / 2, tabY + 10, active ? GOLD : 0xFFE0D2BE);
        }

        int bodyY = tabY + 40;
        int bodyH = h - 155;
        int leftW = 154;
        int rightW = 202;
        int centerX = x + leftW + 20;
        int centerW = w - leftW - rightW - 48;

        // Left information panel
        panel(g, x + 10, bodyY, leftW, bodyH, 0xFF493622, BRASS);
        g.drawString(font, TABS[selectedTab].toUpperCase(), x + 20, bodyY + 14, GOLD, false);
        drawWrapped(g, description(), x + 20, bodyY + 38, leftW - 20, 0xFFFFE8C7);
        g.drawString(font, "НАГРАДЫ ВЕТКИ", x + 20, bodyY + bodyH - 66, GOLD, false);
        g.drawString(font, "Прогресс: " + (selectedNode * 12 + 12) + "%", x + 20, bodyY + bodyH - 43, 0xFFFFFFFF, false);
        g.fill(x + 20, bodyY + bodyH - 25, x + leftW - 18, bodyY + bodyH - 19, 0xFF241B13);
        g.fill(x + 20, bodyY + bodyH - 25, x + 20 + (leftW - 38) * (selectedNode + 1) / 5, bodyY + bodyH - 19, GOLD);

        // Main research board
        panel(g, centerX, bodyY, centerW, bodyH, 0xFF211B15, BRASS);
        for (int gx = centerX + 18; gx < centerX + centerW - 10; gx += 24)
            g.fill(gx, bodyY + 12, gx + 1, bodyY + bodyH - 12, 0xFF382C20);
        for (int gy = bodyY + 16; gy < bodyY + bodyH - 10; gy += 24)
            g.fill(centerX + 10, gy, centerX + centerW - 10, gy + 1, 0xFF382C20);

        int nodeY = bodyY + 52;
        int usable = centerW - 54;
        int step = Math.max(48, usable / 5);
        for (int i = 0; i < 5; i++) {
            int nx = centerX + 18 + i * step;
            int ny = nodeY + (i % 2 == 0 ? 0 : 66);
            if (i < 4) {
                int nextX = centerX + 18 + (i + 1) * step;
                int nextY = nodeY + ((i + 1) % 2 == 0 ? 0 : 66);
                g.fill(nx + 34, ny + 16, nextX, ny + 19, i <= selectedNode ? GOLD : 0xFF8A755D);
            }
            boolean unlocked = i <= selectedNode;
            panel(g, nx, ny, 36, 36, unlocked ? 0xFF604322 : 0xFF39332D, unlocked ? GOLD : 0xFF8B8175);
            centered(g, unlocked ? "✦" : "?", nx + 18, ny + 13, unlocked ? 0xFFFFD078 : 0xFFB5AAA0);
            centered(g, "0" + (i + 1), nx + 18, ny + 42, MUTED);
        }
        g.drawString(font, "ВЫБЕРИТЕ ИССЛЕДОВАНИЕ", centerX + 16, bodyY + bodyH - 28, MUTED, false);

        // Details panel
        int rx = centerX + centerW + 10;
        panel(g, rx, bodyY, rightW, bodyH, PANEL, BRASS);
        g.drawString(font, page.equals("tree") ? NODES[selectedTab][selectedNode] : page.toUpperCase(), rx + 12, bodyY + 14, GOLD, false);
        drawWrapped(g, page.equals("tree")
                ? "Изучите технологию, выполните требования и откройте новые инженерные разработки."
                : "Раздел архива. Здесь будут отображаться ваши записи и доступные материалы.", rx + 12, bodyY + 38, rightW - 20, 0xFFE8D9C5);
        g.drawString(font, "ТРЕБОВАНИЯ", rx + 12, bodyY + 112, GOLD, false);
        g.drawString(font, "• Изучить предыдущий узел", rx + 12, bodyY + 130, 0xFFBFE09A, false);
        g.drawString(font, "• Собрать компоненты", rx + 12, bodyY + 147, 0xFFE8D9C5, false);
        g.drawString(font, "НАГРАДЫ", rx + 12, bodyY + 183, GOLD, false);
        g.drawString(font, "• Новый чертёж", rx + 12, bodyY + 201, 0xFFE8D9C5, false);
        g.drawString(font, "• Доступ к рецепту", rx + 12, bodyY + 218, 0xFFE8D9C5, false);
        panel(g, rx + 10, bodyY + bodyH - 42, rightW - 20, 30, 0xFF70491F, GOLD);
        centered(g, "ИССЛЕДОВАТЬ", rx + rightW / 2, bodyY + bodyH - 32, 0xFFFFE6B7);

        // Footer navigation
        int fy = y + h - 38;
        panel(g, x + 10, fy, 125, 28, PANEL, BRASS);
        centered(g, "ЗАДАНИЯ", x + 72, fy + 9, page.equals("quests") ? GOLD : 0xFFE8D9C5);
        panel(g, x + 142, fy, 125, 28, PANEL, BRASS);
        centered(g, "ЧЕРТЕЖИ", x + 204, fy + 9, page.equals("blueprints") ? GOLD : 0xFFE8D9C5);
        centered(g, "ИССЛЕДУЙТЕ. СОЗДАВАЙТЕ. РАЗВИВАЙТЕСЬ.", x + w / 2, fy + 10, MUTED);
        super.render(g, mouseX, mouseY, partialTick);
    }

    private String description() {
        return switch (selectedTab) {
            case 0 -> "Основа прогресса. Изучайте движение, передачи и первые механизмы.";
            case 1 -> "Переработка ресурсов, сплавы, заводы и складские системы.";
            case 2 -> "Инструменты, защитная экипировка и механические улучшения.";
            case 3 -> "Конструкции, двигатели и воздушные инженерные комплексы.";
            default -> "Исследования, эксперименты и уникальные разработки.";
        };
    }

    private void panel(GuiGraphics g, int x, int y, int w, int h, int fill, int border) {
        g.fill(x, y, x + w, y + h, border);
        g.fill(x + 2, y + 2, x + w - 2, y + h - 2, fill);
        g.fill(x + 4, y + 4, x + w - 4, y + 5, 0x337F674A);
    }

    private void centered(GuiGraphics g, String text, int cx, int y, int color) {
        g.drawCenteredString(font, text, cx, y, color);
    }

    private void drawWrapped(GuiGraphics g, String text, int x, int y, int maxWidth, int color) {
        StringBuilder line = new StringBuilder();
        int yy = y;
        for (String word : text.split(" ")) {
            String candidate = line.length() == 0 ? word : line + " " + word;
            if (font.width(candidate) > maxWidth && line.length() > 0) {
                g.drawString(font, line.toString(), x, yy, color, false);
                yy += 12;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(candidate);
            }
        }
        if (!line.isEmpty()) g.drawString(font, line.toString(), x, yy, color, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int w = Math.min(920, this.width - 24);
            int h = Math.min(510, this.height - 24);
            int x = (this.width - w) / 2;
            int y = (this.height - h) / 2;
            int tabY = y + 76;
            int tabW = (w - 28) / 5;
            if (mouseY >= tabY && mouseY <= tabY + 30) {
                int index = (int) ((mouseX - (x + 10)) / tabW);
                if (index >= 0 && index < 5) {
                    selectedTab = index;
                    selectedNode = 0;
                    page = "tree";
                    return true;
                }
            }
            int fy = y + h - 38;
            if (mouseY >= fy && mouseY <= fy + 28) {
                if (mouseX >= x + 10 && mouseX <= x + 135) { page = "quests"; return true; }
                if (mouseX >= x + 142 && mouseX <= x + 267) { page = "blueprints"; return true; }
            }
            int bodyY = tabY + 40;
            int centerX = x + 174;
            int centerW = w - 154 - 202 - 48;
            int step = Math.max(48, (centerW - 54) / 5);
            for (int i = 0; i < 5; i++) {
                int nx = centerX + 18 + i * step;
                int ny = bodyY + 52 + (i % 2 == 0 ? 0 : 66);
                if (mouseX >= nx && mouseX <= nx + 36 && mouseY >= ny && mouseY <= ny + 36) {
                    selectedNode = i;
                    page = "tree";
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
