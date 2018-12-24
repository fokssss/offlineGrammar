package x.grammar.py;

public class Score implements Comparable {
    X4Word x4Word;
    int score;


    public Score() {

    }

    public Score(int s) {
        this.score = s;
    }


    public X4Word getX4Word() {
        return x4Word;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Score) {
            return score - ((Score) o).score;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "{" +
                "x4Word=" + x4Word +
                ", score=" + score +
                '}';
    }
}