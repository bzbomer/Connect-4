/*
OMER BOZABA
CSE 241
Object Oriented Programming
Fall 2017
 */


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ConnectFour {
    //inner Cell class
    private class Cell{
        //variables
        private int row;
        private int col;
        private char hucre;
        //Constructors
        public Cell(){
            hucre = '.';
        }
        public Cell(int x,int y,char c){
            row = x;
            col = y;
            hucre = c;
        }
        //setterCell
        public void set(char c){
            hucre = c;
        }
        //Getters
        public int getRow(){ return row; }
        public int getCol(){ return col; }
        public char getValue(){return hucre;}
    }//end Cell Class

    //variables
    private Cell gameCells[][];
    private int player;
    private int size;
    private int end;
    private int oyunModu;
    private int hamleSayisi;


    ConnectFour(){
        player = 1;
        size = 4;
        hamleSayisi=0;
        oyunModu = 1;
        end =0;

        gameCells = new Cell[size][size];

    }

    ConnectFour(int n,int gameMod){
       //init
        player = 1;
        size = n;
        hamleSayisi=0;
        oyunModu = gameMod;
        end =0;

        gameCells = new Cell[size][size];

        for(int i=0; i<size; ++i)
            for (int j=0 ; j<size ; ++j) {
                gameCells[i][j]= new Cell(i,j,'.');
            }
    }
    //setters
    public void setOyunModu(int mod) 	{	oyunModu= mod;		}
    public void setHamleSayisi(int hS)  {	hamleSayisi=hS; if(hamleSayisi>=size*size) setGameEnd(); }
    public void setPlayer(int pl)		{	player=pl;			}
    public void setGameEnd()			{	end=1; 				}
    public void yaz(int i,int j,char harf){   gameCells[i][j].set(harf); }
    //getters
    public int currentWidth()    {	return size;	    }
    public int currentHeight()   {	return size;	    }
    public int getOyunModu()     {	return oyunModu;	}
    public int getHamleSayisi()  {	return hamleSayisi;	}
    public int getPlayer()       {	return player; 		}
    public int isGameEnd()       {	return end;			}
    public char isCell(int i,int j){return gameCells[i][j].getValue();}

    //control functions
    private int yatayKontrol(char harf,int test){
        int i=0,j=0,find=0;

        for(i=0;i<currentHeight(); ++i)
            for(j=0;j+3<currentWidth(); ++j)
                if( gameCells[i][j].getValue() == harf &&
                        gameCells[i][j+1].getValue() == harf &&
                        gameCells[i][j+2].getValue() == harf &&
                        gameCells[i][j+3].getValue() == harf
                        ){
                    find = 1;
                }
        return find;
    }

    private int dikeyKontrol(char harf,int test){

        int i=0,j=0,find=0;

        for(j=0;j<currentWidth();++j)
            for(i=0;i+3<currentHeight();++i)
                if( gameCells[i][j].getValue() == harf &&
                        gameCells[i+1][j].getValue() == harf &&
                        gameCells[i+2][j].getValue() == harf &&
                        gameCells[i+3][j].getValue() == harf
                        ){
                    find =1;
                }
        return find;
    }
    private int sagCaprazKontrol(char harf,int test){
        int i=0,j=0,find=0;

        for(i=0;i+3<currentHeight();++i)
            for(j=0;j+3<currentWidth();++j)
                if( gameCells[i][j].getValue() == harf &&
                        gameCells[i+1][j+1].getValue() == harf &&
                        gameCells[i+2][j+2].getValue() == harf &&
                        gameCells[i+3][j+3].getValue() == harf
                        ){
                    find = 1;
                }
        return find;
    }
    private int solCaprazKontrol(char harf,int test){
        int i=0,j=0,find=0;

        for(i=currentHeight()-4;i>=0;--i)
            for(j=currentWidth()-1;j-3>=0;--j) {
                if (gameCells[i][j].getValue() == harf &&
                        gameCells[i + 1][j - 1].getValue() == harf &&
                        gameCells[i + 2][j - 2].getValue() == harf &&
                        gameCells[i + 3][j - 3].getValue() == harf
                        ) {
                    find = 1;
                }
            }
        return find;
    }
    //4 farkli yonden control eder boardu
    public int controls(char harf,int test){

        int kazanan=0 , sonuc;

        sonuc = (yatayKontrol(harf,test)   +
                dikeyKontrol(harf,test)    +
                sagCaprazKontrol(harf,test)+
                solCaprazKontrol(harf,test)
        );
        if(sonuc!=0){

            if(harf == 'X')
                kazanan=1;
            else
                kazanan=2;
            if(test==0)
                setGameEnd();
        }
        return kazanan;
    }

    private int isLegal(char coordinate){

        int i=0,j=0;

        j = coordinate - 'a';/*sutun hesaplama*/
        /*sutun degeri hatali girilmis ise*/
        if(j<0 || j >= currentWidth()){
            return -1;
        }
        /*asagidan yukari dogru , o sutunde ilk bulunan bos yer return edilir*/
        for(i=size -1 ; i>=0 ; --i)
            if(gameCells[i][j].getValue() == '.')
                return i;
        return -1;/*sutun dolu ise -1 return edilir*/
    }

    private char oyunBiterMi(char harf){

        int i=0,j=0,row,col,bitis=1;/*bitis 1  CPU , bitis 2  user icin kontrol*/

        char hamle='a';

        if (harf=='O')/*harf 'X' ise bitis=1 , harf 'O' ise bitis=2*/
            bitis = 2;

        for(i=0; i<currentWidth() ;++i){
            row=isLegal(hamle);
            if(row!=-1){
                col=hamle - 'a';
                gameCells[row][col].set(harf);
                if(controls(harf,1)==bitis){
                    gameCells[row][col].set('.');
                    return hamle;
                }
                else
                    gameCells[row][col].set('.');
            }
            ++hamle;
        }
        return '0';
    }

    public char play(){

        int i=0,row,col;
        char hamle='a',hamle2;
        /*hamle2 ,  2 hamle sonrasini hesaplamak icin*/

        /*CPU oyunu tek hamlede bitirebilir mi?*/
        hamle = oyunBiterMi('O');
        if(hamle != '0')
            return hamle;

        /*Oyuncu oyunu tek hamlede bitirebilir mi?*/
        hamle = 'a';
        hamle = oyunBiterMi('X');
        if(hamle != '0')
            return hamle;

        /*2 hamle Ä°lerisi iÃ§in atak hesaplamasÄ±*/
        hamle = 'a';
        for(i=0 ; i<currentWidth(); ++i){
            row=isLegal(hamle);
            if(row!=-1){
                col=hamle - 'a';
                gameCells[row][col].set('O');/*Eger buraya hamle yaparsa*/
                hamle2 = oyunBiterMi('O');/*burada oyun tek hamlelik mi olur?*/
                gameCells[row][col].set('.'); /*Oyle ise hamle'yi return eder*/
                if(hamle2 != '0')
                    return hamle;
            }
            ++hamle;
        }

	/*2 hamle sonrasinda oyun kazanilabilecek durumda
	  degil ise random bir hamle yapar*/
        //srand(time(NULL));
        System.out.println("yapay");
        do{
            Random rand = new Random();
            col = rand.nextInt(size);
            System.out.println("COL :" + col);
            hamle = 'a';
            for (i=0 ;i<col; ++i)
                ++hamle;        /*karakter artirma*/
            System.out.println(hamle);
            if(isLegal(hamle)!= -1)
                return hamle;
        }while(true);/*return edene kadar hamle arar*/
    }

}
