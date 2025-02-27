package TaskManagement;

class IdGenerator {
    private static int nextId = 0;

    public static int generateId() {
        return nextId++;
    }
}
