package vision.pathfinding.knowledge;

public class Stats {

    int count = 0;
    double sum = 0;

    void add(double num) {
        sum += num;
        count++;
    }

    double avg() {
        return sum / count;
    }
}
