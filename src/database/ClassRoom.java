package database;

public class ClassRoom {
    private int classId;
    private String className;

    // Constructors
    public ClassRoom() {}

    public ClassRoom(int classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    // Getters và Setters
    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
