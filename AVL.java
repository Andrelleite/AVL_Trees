package com.company;

import java.io.IOException;
import java.util.*;

class Node{

    public int height;
    public String word = "";
    public int[] lines;
    public Node left, right;
    private int cardinalL;

    public Node(String w,int line){
        this.height = 1;
        this.word = w;
        this.lines = new int[1];
        this.lines[0] = line;
        cardinalL = 1;
    }

    public void addNewLine(int value){
        int out = 0;
        for(int i = 0; i < cardinalL && out != 1; i++){
            if(this.lines[i] == value){
                out = 1;
            }
        }
        if(out != 1){
            int[] novo = new int[this.lines.length+1];
            for(int j = 0; j < this.lines.length; j++){
                novo[j] = this.lines[j];
            }
            novo[this.lines.length] = value;
            this.lines = novo;
            this.cardinalL++;
        }
    }

}

class Avl {

    Node root;

    public int NodeHeight(Node no) {
        if (no == null){
            return 0;
        }
        return no.height;
    }

    public Node getNewHeight(Node no){
        if(NodeHeight(no.left) > NodeHeight(no.right)){
            no.height =  1 + NodeHeight(no.left);
        }else{
            no.height =  1 + NodeHeight(no.right);
        }
        return no;
    }

    public int best(int h1, int h2) {
        if(h1 > h2){
            return h1;
        }
        return h2;
    }

    public Node rightRotate(Node y) {

        Node temp1 = y.left;
        Node temp2 = temp1.right;
        temp1.right = y;
        y.left = temp2;
        y.height = best(NodeHeight(y.left), NodeHeight(y.right)) + 1;
        temp1.height = best(NodeHeight(temp1.left), NodeHeight(temp1.right)) + 1;

        return temp1;
    }

    public Node leftRotate(Node x) {

        Node temp1 = x.right;
        Node temp2 = temp1.left;
        temp1.left = x;
        x.right = temp2;
        x.height = best(NodeHeight(x.left), NodeHeight(x.right)) + 1;
        temp1.height = best(NodeHeight(temp1.left), NodeHeight(temp1.right)) + 1;

        return temp1;
    }

    public Node insert(Node node, String word, int lines) {

        if (node == null){
            Node novo = new Node(word,lines);
            return (novo);
        }

        if (node.word.compareTo(word) < 0){
            node.left = insert(node.left, word,lines);
        }else if (node.word.compareTo(word) > 0){
            node.right = insert(node.right, word,lines);
        }
        else{
            node.addNewLine(lines);
            return node;
        }

        node = getNewHeight(node);
        int balance = NodeHeight(node.left) - NodeHeight(node.right);

        if(balance > 1){
            if(node.word.compareTo(word) < 0){
                return rightRotate(node);
            }else if(node.word.compareTo(word) > 0){
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }else if(balance < -1){
            if (node.word.compareTo(word) < 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }else if(node.word.compareTo(word) > 0){
                return leftRotate(node);
            }
        }

        return node;
    }

    public String findNode(Node no, String token, String lines, String flag){

        Node search = no;
        int i = 0;
        boolean out = false;
        String specifics = "";

        if(no == null){
            if(flag.equals("lines")){
                return "-1\n";
            }else{
                return "NAO ENCONTRADA.\n";
            }
        }

        if(no.word.compareTo(token) < 0){
            specifics = findNode(no.left,token,lines,flag);
        }else if(no.word.compareTo(token) > 0){
            specifics = findNode(no.right,token,lines,flag);
        }else{
            for(; i < no.lines.length-1; i++){
                specifics += no.lines[i]+" ";
                if(String.valueOf(no.lines[i]).equals(lines)){
                    out = true;
                }
            }
            specifics += no.lines[i]+"\n";
            if(String.valueOf(no.lines[i]).equals(lines)){
                out = true;
            }

            if(flag.equals("assoc")){
                if(out){
                    return "ENCONTRADA.\n";
                }else{
                    return "NAO ENCONTRADA.\n";
                }
            }

            if( i != 0 ){
                return specifics;
            }else{
                return "-1\n";
            }

        }
        return specifics;
    }

}


public class Main{

    static String readLn (int maxLg) {
        byte lin[] = new byte[maxLg];
        int lg = 0, car = -1;
        String line = "";
        try {
            while (lg < maxLg) {
                car = System.in.read();
                if ((car < 0) || (car == '\n')) break;
                lin[lg++] += car;
            }
        } catch (IOException e) {
            return (null);
        }
        if ((car < 0) && (lg == 0)) return (null);

        return (new String(lin, 0, lg));
    }

    public static void main(String[] args) {

        String regex = "";
        int end = 1;
        int lines = -1;
        StringTokenizer st;
        Avl avl = new Avl();
        int assoc = 0;
        ArrayList<String> reply = new ArrayList<String>();
        ArrayList<String[]> tokenSaver = new ArrayList<String[]>();

        regex = readLn(1024);
        if(regex!=null){
            st = new StringTokenizer(regex," ");
            if(st.nextToken().equals("TEXTO") && regex != "") {
                while (end != 0) {
                    lines++;
                    regex = readLn(1024);
                    if(regex!=null){
                        st = new StringTokenizer(regex, "\\,\\.\\;\\~\"\\(\\) ");
                        while (st.hasMoreTokens()) {
                            String token = st.nextToken();
                            if(token.equals("FIM")){
                                end = 0;
                                break;
                            }else{
                                avl.root = avl.insert(avl.root,token,lines);
                                end = 1;
                            }
                        }
                    }
                }
            }
        }


        System.out.println("GUARDADO.");
        int post = 0, switcher;
        st = null;

        while(post == 0 && regex != ""){

            regex = readLn(1024);
            if(regex!=null){
                String header = "";
                try{
                    st = new StringTokenizer(regex," ");
                    header = st.nextToken();
                }catch (NoSuchElementException e){
                    System.out.printf("");
                }
                String[] tokenS = new String[2];
                switcher = 0;

                if(header.equals("TCHAU")){
                    for(int  i = 0; i < reply.size(); i++){
                        System.out.printf(reply.get(i));
                    }
                    post = 1;
                }else if(header.equals("LINHAS")){
                    assoc = 0;
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        reply.add(avl.findNode(avl.root,token,"0","lines"));
                    }
                }else if(header.equals("ASSOC")){
                    assoc = 1;
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        tokenS[switcher] = token;
                        switcher++;
                    }
                }
                if(assoc == 1){
                    reply.add(avl.findNode(avl.root,tokenS[0],tokenS[1],"assoc"));
                    assoc = 0;
                }

            }

        }

    }

}
