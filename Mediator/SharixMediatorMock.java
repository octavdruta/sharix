package Mediator;

import GUI.*;
import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class SharixMediatorMock {
    private SharixGUI gui;

    public SharixMediatorMock() {
        gui = new SharixGUI();
    }

    public void gogogo() {
        gui.buildGUI();
    }

    public void simulate() {
        try {
            (new ListWorker()).doInBackground();
        } catch(Exception e) {}
    }

    public static void main(String[] args) {
        final SharixMediatorMock mock = new SharixMediatorMock();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mock.gogogo();
            }
        });
        mock.simulate();
    }

    private class ListWorker extends SwingWorker<Void, Integer> {
        public ListWorker() { }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                Thread.sleep(3000);
            } catch(InterruptedException e) { }
            publish(1);
            return null;
        }

        @Override
        protected void process(List<Integer> list) {
            for (Integer i : list) {
                switch(i.intValue()) {
                case 1:
                    gui.updateTransfer("Vlad", "Andrei", "file.txt", "sending", 60);
                    break;
                }
            }
        }

        @Override
        public void done() {
        }
    }
}
