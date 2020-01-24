/*
OMER BOZABA
CSE 241
Object Oriented Programming
Fall 2017
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class main {

    public static  void  main(String [] argv){

        String mod,str;
        char c;
        int n,oyunModu=1;

        do{//oyun modu alma
            while( ( mod = JOptionPane.showInputDialog("Oyun Modu:  'P' or 'C'")).length() != 1 );
            c= mod.charAt(0);
        }while(c!='C' && c!='P');

        //size input alma
        do{
            str = JOptionPane.showInputDialog("Size Giriniz: ");
            n = Integer.parseInt(str);
        }while(n<4);
        //oyun modunu integer veriye cevirme , CF objesi icin
        if(c == 'C')
            oyunModu=1;
        else if(c == 'P')
            oyunModu=2;

        //obje yapiliyor
        ConnectFour obj= new ConnectFour(n,oyunModu);

        //JFrame olusturma , size ve gorunurluk vs
        JFrame frame = new JFrame("Connect Four");
        frame.setSize(57*n,32*(n+2));
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Jpanel olusturma GUi adli . pdften alinti
        JPanel GUI = new JPanel();
        GUI.setLayout(null);
        //baska panel olusturup onu Gui paneline ekliyoruz daha sonra, butonlarla birlikte
        JPanel dugmeler = new JPanel();
        dugmeler.setLayout(null);
        dugmeler.setLocation(0,0);
        dugmeler.setSize(n*57,(n+2)*32);


        /*Buton ekleme kismi
          https://www.youtube.com/watch?v=1i9WBVnTwHA
          buradan alinti , orada 1 buton ekliyordu ben dinamik yaptim
          (n) kadar buton
         */
        JButton jb[] = new JButton[n];
        int mesafe = 0;
        c='A';
        for(int i=0; i < n ; i++){
            jb[i] = new JButton();
            String s=""+c;
            jb[i].setText(s);
            jb[i].setSize(55,20);
            jb[i].setLocation(mesafe ,0);
            jb[i].setHorizontalAlignment(0);
            dugmeler.add(jb[i]);//dugmeler paneline ekliyoruz
            mesafe += 55;
            ++c;
        }
        //oyun ekraninda kutular icin label kullandim. 3 farkli gif , bos ,sari ve kirmizi
        JLabel  labels[][] = new JLabel[n][n];
        int yukseklik=20;
        for (int i=0; i<n ; ++i){
            mesafe = 0;
            for(int j=0; j<n; ++j){
                labels[i][j] = new JLabel();
                labels[i][j].setIcon(new ImageIcon("bos.gif"));
                labels[i][j].setLocation(mesafe,yukseklik);
                labels[i][j].setSize(55,20);
                labels[i][j].setVisible(true);
                mesafe += 55;
                dugmeler.add(labels[i][j]);
            }
            yukseklik += 20;
        }

        //oyunun durumunu en altta gosteren label.  Oyunun bitip bitmedigini soyluyor
        JLabel sonuc = new JLabel("Player 1");
        sonuc.setLocation( (n+1)*23,(n+1)*20);
        sonuc.setSize(100,20);
        dugmeler.add(sonuc);//labeli dugmelere
        GUI.add(dugmeler);//dugmeler de gui'ye add yapildi
        frame.setContentPane(GUI);
        frame.setVisible(true);


        //buton click durumlari
        for (int i=0; i<n ; ++i){
            jb[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    int kazanan = 0;
                    char hamle = e.getActionCommand().charAt(0);//click edilen butonun column olarak atanmasi A,B,C,D vs.
                    int j = hamle - 'A';//col. belirleme
                    for (int i = obj.currentHeight() - 1; i >= 0 ; i--) {//o sutunda bos yerlerden en alttakinin row'u bulunur
                        if (obj.isCell(i,j) == '.') {//o koordinat bos mu ?
                            obj.setHamleSayisi(obj.getHamleSayisi()+1);//hamle sayisi artirma
                            if(obj.getPlayer()==1) {//player 1 hamle sirasi ise
                                labels[i][j].setIcon(new ImageIcon("red.gif"));//label renk degisimi
                                obj.setPlayer(0);//sira degisimi
                                sonuc.setText("Player 2");
                                obj.yaz(i,j,'X');
                                kazanan = obj.controls('X',0);//kazanildi mi? kontrol
                            }
                            else {//oyun sirasi player 2 de ise
                                obj.yaz(i,j,'O');
                                labels[i][j].setIcon(new ImageIcon("yellow.gif"));
                                obj.setPlayer(1);//sira degisimi
                                sonuc.setText("Player 1");
                                kazanan = obj.controls('O',0);
                            }

                            break;//donguden cikilir
                        }

                    }//end for
                    if(kazanan == 1){//player 1 kazanma durumu
                        JOptionPane.showMessageDialog(null,"Player 1 Kazandi" ,"Oyun Sonucu",JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                    else if(kazanan == 2){//player 2 kazanma durumu
                        JOptionPane.showMessageDialog(null,"Player 2 Kazandi" ,"Oyun Sonucu",JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }

                    else {//player 1'in pesine Computer oynar
                        if (obj.getOyunModu() == 1 && obj.getPlayer()==0){
                            hamle = obj.play();//objenin yapay zeka fonksiyonu hamle hesaplar
                            j= hamle-'a';
                            for (int i = obj.currentHeight() - 1; i >= 0; i--) {
                                if (obj.isCell(i, j) == '.') {
                                    obj.yaz(i, j, 'O');
                                    labels[i][j].setIcon(new ImageIcon("yellow.gif"));
                                    obj.setPlayer(1);
                                    sonuc.setText("Player 1");
                                    break;
                                }
                            }
                        obj.setHamleSayisi(obj.getHamleSayisi() + 1);// hamlesayisi++
                        }
                        kazanan = obj.controls('O',0);
                        if(kazanan == 2) {//Yapay zeka kazanma durumu
                            JOptionPane.showMessageDialog(null, "Computer Kazandi", "Oyun Sonucu", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        }
                    }
                    if(obj.isGameEnd()==1) {//oyunun bittiginde sonuc labeli ne yazacak?
                        if(kazanan == 0) {
                            sonuc.setText("Oyun BERABERE BITTI");
                            JOptionPane.showMessageDialog(null, "Oyun BERABERE BITTI", "Oyun Sonucu", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        }
                        else
                            sonuc.setText("Oyun BITTI");
                    }
                }
            });
        }

    }


}
