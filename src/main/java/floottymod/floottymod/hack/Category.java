package floottymod.floottymod.hack;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    MACRO("Macro"),
    QOL("QOL"),
    RENDER("Render");

    public String name;

    private Category(String name) {
        this.name = name;
    }
}
