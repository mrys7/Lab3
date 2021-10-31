package lab3;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.Scanner;

class Czasomierz extends Thread {
    int godz,min,sec;
    String nazwa;
    public Czasomierz(int godz, int min, int sec, String nazwa) {
        this.godz = godz;
        this.min = min;
        this.sec = sec;
        this.nazwa = nazwa;
    }

    public void run() {
        while(true) {
            System.out.println(this.nazwa + " - " + this.godz + ":" + this.min + ":" + this.sec);
            this.sec++;
            try {
                Thread.sleep(1000);
            }catch(Exception e) {

            }
            if(this.sec == 60) {
                this.sec = 0;
                this.min++;
                if(this.min == 60) {
                this.min = 0;
                this.godz++;
                }
                if(this.godz == 24)
                this.godz = 0;
            }   
        }
    }
}

class Samochod extends Thread{
    private String nrRej;
    private int pojZbiornika;
    private int paliwo;
    private Random r = new Random();

    public Samochod(String nrRej,  int pojZbiornika) {
        this.nrRej = nrRej;
        this.pojZbiornika = pojZbiornika;
        this.paliwo = r.nextInt(20)+11;
    }
    public void tankowanie(int _paliwo) {
        this.paliwo += _paliwo;
        System.out.println(this.nrRej + " - tankowanie. - " + _paliwo);
    }

    @Override
    public void start() {
        System.out.println(this. nrRej + " wyjechał.");
        super.start();
    }

    @Override
    public void run() {
        while(true) {
            try {
                if (this.paliwo <= 10) {
                    System.out.println(this.nrRej + " - malo paliwa.");
                    this.tankowanie(this.pojZbiornika - this.paliwo - r.nextInt(15)+3);
                    Thread.sleep(2000);
                } else {
                    System.out.println(this.nrRej + " jest w drodze. - " + this.paliwo + "/" + this.pojZbiornika);
                    this.paliwo -= 1;
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
            }
        }
    }
}
class PoleKolaMC extends Thread {
    String nazwa;
    double PoczX, PoczY, KonX, KonY;
    int ilosc;
    double wynik;
    Random r = new Random();

    public PoleKolaMC(double PoczX, double PoczY, double KonX, double KonY, int ilosc, String nazwa) {
        this.nazwa = nazwa;
        this.PoczX = PoczX;
        this.PoczY = PoczY;
        this.KonX = KonX;
        this.KonY = KonY;
        this.wynik = 0;
        this.ilosc = ilosc;
    }

    public void run() {
        int PunktyNalezace = 0;

        for (int i = 0; i < this.ilosc; i++) {
            double x = Math.random();
            double y = Math.random();

            if ((x * x + y * y) <= 1)
                PunktyNalezace++;
        }
        double PoleKwadratu = Math.pow(Math.sqrt(((this.KonX - this.PoczX)*(this.KonX - this.PoczX)) + ((this.KonY - this.PoczY)*(this.KonY - this.PoczY))), 2) / 2;
        this.wynik = PunktyNalezace * PoleKwadratu / this.ilosc;
        System.out.println(this.nazwa + " - pole: " + this.wynik);
    }

    public double getWynik() {
        return this.wynik;
    }

}


class Zespolona {
    private double rzeczywista;
    private double urojona;

    public Zespolona(double rzeczywista, double urojona){
        this.rzeczywista = rzeczywista;
        this.urojona = urojona;
    }
    public double mod(){
        return Math.sqrt(rzeczywista * rzeczywista + urojona * urojona);
    }
    public Zespolona dodaj(Zespolona inna){
        return new Zespolona(this.rzeczywista + inna.rzeczywista, this.urojona + inna.urojona);
    }
    public Zespolona Kwadrat(){
        double r = (this.rzeczywista * this.rzeczywista) - (this.urojona * this.urojona);
        double u = (this.rzeczywista * this.urojona) + (this.urojona * this.rzeczywista);
        return new Zespolona(r, u);
    }
}

class Negatyw extends Thread{
    BufferedImage obraz;
    int PoczX, PoczY, KonX, KonY;

    public Negatyw(BufferedImage obraz, int PoczX, int PoczY, int KonX, int KonY){
        this.PoczX = PoczX;
        this.PoczY = PoczY;
        this.KonX = KonX;
        this.KonY = KonY;
        this.obraz = obraz;
    }

    @Override
    public void run() {
        for(int i = PoczX; i < KonX; i++){
            for(int j = PoczY; j < KonY; j++) {
                Color c = new Color(obraz.getRGB(i, j));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                int newR, newG, newB;
                newR = 255 - r;
                newG = 255 - g;
                newB = 255 - b;
                Color newColor = new Color(newR, newG, newB);
                obraz.setRGB(i, j, newColor.getRGB());
            }
        }
    }
}

class FJulia  extends Thread{
    int start;
    int stop;
    int N;
    double X;
    double Y;
    int[][] tab;
    public FJulia(int i, int N, int[][] tab) {
        this.start = (N / 4) * i;
        this.stop = (N / 4) * (i + 1);
        this.N = N;
        this.X = 2.5 / N;
        this.Y = 2.5 / N;
        this.tab = tab;
    }
    public void run() {
        for (int i = this.start; i < this.stop; i++) {
            for (int j = 0; j < this.N; j++) {
                double rzeczywista = (i * this.Y) - 1.25;
                double urojona = (j * this.X) - 1.25;
                int k = 0;
                Zespolona zespolona1 = new Zespolona(rzeczywista, urojona);
                Zespolona zespolona2 = new Zespolona(-0.70176, -0.3842);
                while(zespolona1.mod() < 2.0 && k < 100) {
                    k++;
                    zespolona1 = zespolona2.dodaj(zespolona1.Kwadrat());
                }
                this.tab[i][j] = k;
            }
        }
    }
}

class Filozofowie1 extends Thread {
    int MAX;
    static Semaphore[] widelec;
    int nr;
    public Filozofowie1 (int nr, int max) {
        this.nr = nr;
        this.MAX = max;
        this.widelec = new Semaphore[MAX] ;
        for ( int i = 0; i < this.MAX; i++) {
            this.widelec[i] = new Semaphore( 1 );
        }
    }
    public void run () {
        while (true) {
            System.out.println(this.nr + " - mysli");
            try {
                Thread.sleep((long) (7000 * Math.random()));
            } catch (InterruptedException e) {
            }
            this.widelec[this.nr].acquireUninterruptibly();
            this.widelec[(this.nr + 1) % this.MAX].acquireUninterruptibly();
            System.out.println(this.nr + " - zaczyna jesc");

            try {
                Thread.sleep((long) (5000 * Math.random()));
            } catch (InterruptedException e) {
            }

            System.out.println(this.nr + " - konczy jesc");
            this.widelec[this.nr].release();
            this.widelec[(this.nr + 1) % this.MAX].release();
        }}
}

class Filozofowie2 extends Thread {
    int MAX;
    static Semaphore [] widelec;
    int nr;
    public Filozofowie2 (int nr, int max) {
        this.nr = nr;
        this.MAX = max;
        this.widelec = new Semaphore[this.MAX];
        for ( int i = 0; i < this.MAX; i++) {
            this.widelec[i] = new Semaphore( 1 );
        }
    }
    public void run () {
        while (true) {
            System.out.println (this.nr + " - mysli");
            try {
                Thread.sleep ((long)(5000 * Math.random()));
            } catch (InterruptedException e) {
            }
            if (this.nr == 0) {
                this.widelec[ (this.nr + 1) % this.MAX].acquireUninterruptibly();
                this.widelec[this.nr].acquireUninterruptibly();
            } else {
                this.widelec[this.nr].acquireUninterruptibly();
                this.widelec[ (this.nr + 1) % this.MAX].acquireUninterruptibly();
            }
            System.out.println(this.nr + " - zaczyna jesc");
            try {
                Thread.sleep((long)(3000 * Math.random()));
            } catch ( InterruptedException e ) {
            }
            System.out.println (this.nr + " - konczy jesc");
            this.widelec[this.nr].release();
            this.widelec[(this.nr + 1) % MAX].release();
        }
    }

}

class Filozofowie3 extends Thread {
    int MAX;
    static Semaphore [] widelec;
    int nr;
    Random r;
    public Filozofowie3(int nr, int max) {
        this.nr = nr ;
        this.r = new Random(this.nr) ;
        this.MAX = max;
        widelec = new Semaphore[this.MAX] ;
        for ( int i = 0; i < this.MAX; i++) {
            this.widelec[i] = new Semaphore( 1 ) ;
        }
    }
    public void run () {
        while (true) {
            System.out.println (this.nr + " - mysli") ;
            try {
                Thread.sleep ( ( long ) (5000 * Math.random( ) ) ) ;
            } catch ( InterruptedException e ) {
            }
            int strona = this.r.nextInt( 2 ) ;
            boolean podnioslDwaWidelce = false ;
            do {
                if (strona == 0) {
                    this.widelec[this.nr].acquireUninterruptibly ();
                    if(!( widelec[(this.nr +1 ) % this.MAX].tryAcquire( ))) {
                        this.widelec[this.nr].release();
                    } else {
                        podnioslDwaWidelce = true ;
                    }
                } else {
                    this.widelec[(this.nr + 1) % this.MAX].acquireUninterruptibly();
                    if (!(this.widelec[this.nr].tryAcquire())) {
                        this.widelec[(this.nr + 1) % this.MAX].release();
                    } else {
                        podnioslDwaWidelce = true;
                    }
                }
            } while ( podnioslDwaWidelce == false);
            System.out.println (this.nr + " - zaczyna jesc") ;
            try {
                Thread.sleep((long)(3000 * Math.random()));
            } catch (InterruptedException e) {
            }
            System.out.println(this.nr + " - konczy jesc") ;
            this.widelec[this.nr].release();
            this.widelec[(this.nr + 1) % this.MAX].release();
        }
    }
}

public class Lab3 {
    static public BufferedImage tworzenieObrazka(int[][] tab, int N, int q) {
        BufferedImage img = new BufferedImage(N, N, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int k = tab[i][j];
                Color c;
                if (k >= q)
                    c = new Color(0, 0, 0);
                else
                    c = new Color(0, (float) k /q, (float) k /q);
                img.setRGB(i, j, c.getRGB());
            }
        }
        return img;
    }
    public static void main(String[] args) {
        Czasomierz stoper1 = new Czasomierz(23, 59, 57, "stoper1");
        Czasomierz stoper2 = new Czasomierz(0, 0, 1, "stoper2");
        Samochod samochod1 = new Samochod("BI 12345", 1000);
        Samochod samochod2 = new Samochod("HALOHALO", 500);
        PoleKolaMC pole1 = new PoleKolaMC(0, 0, 1, 1, 1000, "p1");
        PoleKolaMC pole2 = new PoleKolaMC(10, 10, 17, 17, 500, "p2");
        int N = 4096;
        int q = 100;
        int[][] tab = new int[N][N];
        Scanner scan = new Scanner(System.in);
        
        while(true){
            int zadanie;
            System.out.println("Podaj numer");
            System.out.println("1. Fraktal Julii");
            System.out.println("2. Negatyw");
            System.out.println("3. Pola kol");
            System.out.println("4. Czasomierz + samochody");
            System.out.println("5. Filozofowie");
            zadanie = scan.nextInt();
            switch(zadanie){
                case 1:
                FJulia[] julia = new FJulia[4];
                for(int i = 0; i < 4; i++) {
                    julia[i] = new FJulia(i, N, tab);
                    julia[i].start();
                }
                try {
                    for(int i = 0; i < 4; i++) {
                        julia[i].join();
                    }
                }catch(Exception e) {
                }
                BufferedImage img = Lab3.tworzenieObrazka(tab, N, q);
                try{
                    ImageIO.write(img, "PNG", new File("FJulia.png"));
                }catch(Exception e) { 
                }
                break;

                case 2:
                BufferedImage doNegatywu;
                try{
                    doNegatywu = ImageIO.read(new File("kotek.jpg"));
                    int szer = doNegatywu.getWidth();
                    int wys = doNegatywu.getHeight();
                    Negatyw negatyw1 = new Negatyw(doNegatywu, 0, 0, szer/3, wys);
                    Negatyw negatyw2 = new Negatyw(doNegatywu, 2*szer/3, 0, szer, wys);
                    negatyw1.start();
                    negatyw2.start();
                    try{
                        negatyw1.join();
                        negatyw2.join();
                    }catch(Exception e){
                    }
                    ImageIO.write(doNegatywu, "jpg", new File("negatywnyKotek.jpg"));
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
                
                case 3:
                pole1.run();
                pole2.run();
                break;
                
                case 4:
                stoper1.start();
                stoper2.start();
                samochod1.start();
                samochod2.start();
                break;
                
                case 5:
                int iloscFilozofow = 0;
                int wyborWariantu = 0;
                while(!(iloscFilozofow>2 && iloscFilozofow<100)){
                    System.out.println("Podaj ilosc filozofow (2;100)");
                    iloscFilozofow = scan.nextInt();
                }
                
                while(!(wyborWariantu >= 1 && wyborWariantu <= 3)){
                    System.out.println("Podaj wariant <1;3>");
                    wyborWariantu = scan.nextInt();
                    
                    switch(wyborWariantu){
                        case 1:
                            for(int i = 0; i < iloscFilozofow; i++)
                                new Filozofowie1(i,iloscFilozofow).start();
                        break;
                            
                        case 2:
                            for(int i = 0; i < iloscFilozofow; i++)
                                new Filozofowie2(i,iloscFilozofow).start();
                        break;
                            
                        case 3:
                            for(int i = 0; i < iloscFilozofow; i++)
                                new Filozofowie3(i,iloscFilozofow).start();
                        break;
                    }
                }
                break;
            }
        }
    }
}

