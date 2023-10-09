package view;

import model.Subject;

public interface Observer {
    public void update(Subject subj);

    public void update(Subject subj, Object data);
}
