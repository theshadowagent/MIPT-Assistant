package dgapmipt.pda;

public class Task {
    public String name;
    public int id;
    public String description;
    public String code;

    Task() {
    }

    Task(String name, String description, String code) {
        this.name = name;
        this.description = description;
        this.code = code;
    }
}
