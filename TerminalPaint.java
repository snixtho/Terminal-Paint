/*
 * Made by snixtho
 */

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Scanner;

public class TerminalPaint {
	
	/*
	 * TEGNE METODER
	 */
	
	
	/**
	 * Tegner en linje fra p1 til p2.
	 * 
	 * @param p1 Forste posisjon.
	 * @param p2 Andre posisjon.
	 * @param c Tegn til aa tegne med.
	 * @param lerret Lerret data arrayen.
	 * @param lerretDim Lerret dimensjonen.
	 */
	static void tegnLinje(Point p1, Point p2, int c, int[] lerret, Rectangle lerretDim) {
		double stigning = 0;
		// bestem om algoritmen skal folge x-aksen eller y-aksen, altsa folg lengste akse for best linje kvalitet
		boolean folgX = Math.abs(p2.x - p1.x) >= Math.abs(p2.y - p1.y);
		int lengde = 0;
		Point startp;
		
		// beregn lengde aa stigningstall for linjen.
		if (folgX)
		{
			lengde = Math.abs(p2.x - p1.x);
			stigning = (double)(p2.y - p1.y)/(double)(p2.x - p1.x);
			
			// finn start punktet (den som har kortest avstand fra x- eller y-aksen)
			if (p2.x < p1.x || (p1.x == p2.x && p2.y < p1.y))
			{
				startp = p2;
			}
			else
			{
				startp = p1;
			}
		}
		else
		{
			lengde = Math.abs(p2.y - p1.y);
			stigning = (double)(p2.x - p1.x)/(double)(p2.y - p1.y);
			
			// finn start punktet (den som har kortest avstand fra x- eller y-aksen)
			if (p2.y < p1.y || (p1.x < p2.x && p2.y == p1.y))
			{
				startp = p2;
			}
			else
			{
				startp = p1;
			}
		}
		
		// Beregn y for hver x i lengden mellom p1 og p2, og set c til denne posisjonen.
		if (folgX)
		{ // folg x-aksen
			for (int i = 0; i <= lengde; i++)
			{
				int x = startp.x + i;
				int y = startp.y + (int)Math.round(stigning*(double)i);
				
				if (x >= 0 && x < lerretDim.width && y >= 0 && y < lerretDim.height)
				{
					lerret[XY(x, y, lerretDim)] = c;
				}
			}
		}
		else
		{ // folg y-aksen.
			for (int i = 0; i <= lengde; i++)
			{
				int x = startp.x + (int)Math.round(stigning*(double)i);
				int y = startp.y + i;
				
				if (x >= 0 && x < lerretDim.width && y >= 0 && y < lerretDim.height)
				{
					lerret[XY(x, y, lerretDim)] = c;
				}
			}
		}
	}
	
	/**
	 * Tegner et polygon fra et sett med punkter.
	 * 
	 * @param points Punkter i polygonet.
	 * @param c Tegn til aa tegne med.
	 * @param lerret Lerret data array.
	 * @param lerretDim Lerret storrelse (dimensjoner).
	 */
	static void tegnPolygon(Point[] points, int c, int[] lerret, Rectangle lerretDim) {
		if (points.length >= 3)
		{
			int i;
			for (i = 0; i < points.length; i++)
			{
				if (i+1 < points.length)
				{
					tegnLinje(points[i], points[i+1], c, lerret, lerretDim);
				}
			}
			
			i--;
			
			// bare tegn siste linje hvis punktene ikke kobler sammen.
			if (points[0].x != points[i].x || points[0].y != points[i].y)
			{
				tegnLinje(points[0], points[i], c, lerret, lerretDim);
			}
		}
	}
	
	/**
	 * Tegn en sirkel med en spesifisert radius.
	 * 
	 * @param x X kordinaten til sirkelen.
	 * @param y Y kordinaten til sirkelen.
	 * @param r Radiusen til sirkelen
	 * @param c Tegn til aa tegne med.
	 * @param lerret Lerret data array.
	 * @param lerretDim Lerret dimensjoner.
	 */
	static void tegnSirkel(int x, int y, int r, int c, int[] lerret, Rectangle lerretDim) {
		for (int i = -r; i <= r; i++)
		{
			for (int j = -r; j <= r; j++)
			{
				// bruker pytagoras til aa finne ut om pikselen ligger paa sirkel buen
				if (Math.round(Math.sqrt(i*i + j*j)) == r)
				{
					int rx = j+x;
					int ry = i+y;
					
					// bare modifiser arrayen hvis koordinatene ligger innenfor lerretet
					if ((rx >= 0 && rx < lerretDim.width) && (ry >= 0 && ry < lerretDim.height))
					{
						lerret[XY(rx, ry, lerretDim)] = c;
					}
				}
			}
		}
	}
	
	/**
	 * Tegn en rekangel som starter i pos og har hoyde og bredde dim.
	 * 
	 * @param rect Posisjonen til oppe-venstre hjornet, samt hoyde og bredde.
	 * @param c Tegn til aa tegne med.
	 * @param lerret Lerret data array.
	 * @param lerretDim Lerret dimensjoner.
	 */
	static void tegnRektangel(Rectangle rect, int c, int[] lerret, Rectangle lerretDim) {
		tegnPolygon(new Point[] {
				new Point(rect.x, rect.y),
				new Point(rect.x, rect.y + rect.height),
				new Point(rect.x + rect.width, rect.y + rect.height),
				new Point(rect.x + rect.width, rect.y)
		}, c, lerret, lerretDim);
	}
	
	/**
	 * Tegn et punkt paa lerretet.
	 * 
	 * @param x Punktet's X posisjon.
	 * @param y Punktet's Y posisjon.
	 * @param c Tegn til aa tegne med.
	 * @param lerret Lerret data array.
	 * @param lerretDim Lerret dimensjoner.
	 */
	static void tegnPunkt(int x, int y, int c, int[] lerret, Rectangle lerretDim) {
		if (x >= 0 && x < lerretDim.width && y >= 0 && y < lerretDim.height)
		{ // bare tegn innenfor
			lerret[XY(x, y, lerretDim)] = c;
		}
	}
	
	/**
	 * Visker alt bort fra lerretet.
	 * 
	 * @param lerret Lerret arrayen.
	 * @param lerretDim Dimensjonene til lerretet.
	 */
	static void viskAlt(int[] lerret) {
		for (int i = 0; i < lerret.length; i++)
		{
			lerret[i] = ' ';
		}
	}
	
	/**
	 * Returnerer en posisjon som tilsvarer x, y posisjonen paa lerretet.
	 * 
	 * @param x X-aksen.
	 * @param y Y-aksen.
	 * @param lerretDim Lerret dimensjonene.
	 * @return Korrekt indeks i lerret data arrayen.
	 */
	static int XY(int x, int y, Rectangle lerretDim)
	{
		return y*lerretDim.width + x;
	}
	
	
	/*
	 * KOMMANDO SYSTEM
	 */
	
	
	/**
	 * Returnerer true hvis stringen bare innerholder tall symboler.
	 * 
	 * @param tallStr Tallsymbolene.
	 * @return
	 */
	static boolean erTall(String tallStr) {
		// sjekker hver tegn i stringen mot ASCII-verdiene for tall tegnene
		for (int i = 0; i < tallStr.length(); i++)
		{
			if (tallStr.charAt(i) < 48 || tallStr.charAt(i) > 57)
			{ // stringen innerholder ikke bare tall tegn
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Legg til et element til en String array.
	 * 
	 * @param arr Arrayen som skal faa et nytt element.
	 * @param nyttElement Det nye elementet
	 * @return En ny array med det nye elementet til slutt.
	 */
	static String[] strArrayLeggTil(String[] arr, String nyttElement) {
		String[] arr2 = new String[arr.length + 1];
		
		// kopier over arrayet
		for (int i = 0; i < arr.length; i++)
		{
			arr2[i] = arr[i];
		}
		
		arr2[arr.length] = nyttElement;
		
		return arr2;
	}
	
	/**
	 * Returnerer en array med kommando navn og forklaring.
	 * Oppbyggingen er slik:
	 * Hvis i er en teller for arrayen, som teller til
	 * array array.length/2 blir det slik:
	 * Hvert element strekker seg over 2 elementer slik
	 * at hvert element har index i*2 og i*2 + 1.
	 * Det vil si at kommando navnet ligger paa
	 * indeks i*2 og forklaringen paa i*2 + 1.
	 * 
	 * @return
	 */
	static String[] kommandoListe() {
		return new String[] {
			"drawCircle",
			"Draws a circle with the arguments: <x>, <y>, <radius>, character",
			"drawPolygon",
			"Draws a polygon with the arguments: <x1, y1>, <x2, y2>, <x3, y3>, ..., <xN, yN>, character",
			"drawLine",
			"Draws a line from point 1 (x1, y1) to point 2 (x2, y2) with the arguments: <x1, y1>, <x2, y2>, character",
			"drawPoint",
			"Draw a point with the arguments: <x>, <y>, character",
			"drawRectangle",
			"Draw a rectangle with the arguments: <x, y>, <w, h>, character",
			"exit",
			"Close the program.",
			"help",
			"Print out help information. If you want help for just one command, write help <commandName>.",
			"eraseAll",
			"Erases everything on the canvas.",
			"figure",
			"Draws a figure with the arguments: <figur>, <x>, <y>. Available figures: ifi, windows, hjerte, java"
		};
	}
	
	/**
	 * Legg til et element til en Point array.
	 * 
	 * NOTE: generics eller List hadde vaert fint her :D
	 * 
	 * @param arr Arrayen som skal faa et nytt element.
	 * @param nyttElement Det nye elementet
	 * @return En ny array med det nye elementet til slutt.
	 */
	static Point[] pointArrayLeggTil(Point[] arr, Point nyttElement) {
		Point[] arr2 = new Point[arr.length + 1];
		
		// kopier over arrayet
		for (int i = 0; i < arr.length; i++)
		{
			arr2[i] = arr[i];
		}
		
		arr2[arr.length] = nyttElement;
		
		return arr2;
	}
	
	/**
	 * Parse en kommando og returner en array med kommando navn og argumentene.
	 * 
	 * @param cmd Plain kommando.
	 * @return Argumentene til kommandoen med navnet i forste element.
	 */
	static String[] parseKommando(String cmd) {
		// ta bort unodvendig mellomrom
		cmd = cmd.trim();
		
		// faa navnet
		String[] args = new String[0];
		int navnLen = cmd.indexOf(' ');
		boolean ingenArgs = navnLen < 0;
		
		if (navnLen == -1)
		{ // hvis mellom rom ikke er funnet, er det en kommando uten argumenter
			navnLen = cmd.length();
		}
		
		args = strArrayLeggTil(args, cmd.substring(0, navnLen));
		
		if (!ingenArgs)
		{
			// faa argumentene til kommandoen
			String[] cmdArgs = cmd.substring(navnLen).replaceAll(" ", "").split(",");
			
			if (cmdArgs.length == 0)
			{ // kommandoen har bare ett argument
				args = strArrayLeggTil(args, cmd.substring(navnLen));
			}
			
			// ignorer hvis det ikke finnes argumenter
			if (cmdArgs.length > 0)
			{
				for (String argument : cmdArgs)
				{
					args = strArrayLeggTil(args, argument);
				}
			}
		}
		
		return args;
	}
	
	/**
	 * Kjor en kommando med sine argumenter.
	 * 
	 * @param args Kommandoen med argumentene sine.
	 * @param lerret Lerret data arrayen.
	 * @param lerretDim Lerret dimensjonene.
	 */
	static void kjorKommando(String[] args, int[] lerret, Rectangle lerretDim) {
		boolean success = true;
		
		if (args.length > 0)
		{// sjekk kommando navn, og kjor metoden som tillhorer kommandoen
			
			boolean visLerret = false;
			if (args[0].equals("drawCircle"))
			{
				success = kommandoTegnSirkel(args, lerret, lerretDim);
				visLerret = true;
			}
			else if (args[0].equals("drawPolygon"))
			{
				success = kommandoTegnPolygon(args, lerret, lerretDim);
				visLerret = true;
			}
			else if (args[0].equals("drawLine"))
			{
				success = kommandoTegnLinje(args, lerret, lerretDim);
				visLerret = true;
			}
			else if (args[0].equals("drawPoint"))
			{
				success = kommandoTegnPunkt(args, lerret, lerretDim);
				visLerret = true;
			}
			else if (args[0].equals("drawRectangle"))
			{
				success = kommandoTegnRektangel(args, lerret, lerretDim);
				visLerret = true;
			}
			else if (args[0].equals("figure"))
			{
				success = kommandoFigur(args, lerret, lerretDim);
				visLerret = true;
			}
			else if (args[0].equals("help"))
			{
				success = kommandoHjelp(args, lerret, lerretDim);
			}
			else
			{ // kommando ikke funnet
				pUgyldigKommando(args[0]);
				
				// gaa ut av metoden
				return;
			}
			
			if (!success)
			{ // ugyldig kommando argumenter
				pUgyldigArgumenter(args[0]);
				pKommandoForklaring(args[0]);
			}
			else if (visLerret)
			{ // skriv ut lerretet hvis det var en tegne kommando som kjorte
				printLerret(lerret, lerretDim);
			}
		}
	}
	
	/*
	 * Kommando: tegnSirkel
	 */
	static boolean kommandoTegnSirkel(String[] args, int[] lerret, Rectangle lerretDim) {
		if (args.length == 5)
		{
			if (!erTall(args[1]) || !erTall(args[2]) || !erTall(args[3]))
			{ // tall argumentene er ikke tall
				return false;
			}
			
			// faa argumentene fram og tegn sirkelen
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			int radius = Integer.parseInt(args[3]);
			int tegn = args[4].charAt(0);
			
			tegnSirkel(x, y, radius, tegn, lerret, lerretDim);
			
			// success!
			return true;
		}
		
		return false;
	}
	
	/*
	 * Kommando: tegnSirkel
	 */
	static boolean kommandoTegnPolygon(String[] args, int[] lerret, Rectangle lerretDim) {
		// grunnen til (args.length - 1) % 2 == 1 er for aa
		// sjekk om det er et odde-tall antall argumenter
		// siden et punkt har 2 tall og tegnet er ogsaa et argument.
		if ((args.length - 1) % 2 == 1 && args.length >= 2)
		{
			
			Point[] punkter = new Point[0];
			
			// sjekk punktene og putt dem i arrayen
			for (int i = 1; i < args.length - 1; i += 2)
			{
				if (!erTall(args[i]) || !erTall(args[i+1]))
				{ // noen av punktene innerholder ikke tall
					return false;
				}
				
				int x = Integer.parseInt(args[i]);
				int y = Integer.parseInt(args[i+1]);
				
				punkter = pointArrayLeggTil(punkter, new Point(x, y));
			}
		
			int tegn = args[args.length-1].charAt(0);
			
			tegnPolygon(punkter, tegn, lerret, lerretDim);
			
			// success!
			return true;
		}
		
		return false;
	}
	
	/*
	 * Kommando: tegnLinje
	 */
	static boolean kommandoTegnLinje(String[] args, int[] lerret, Rectangle lerretDim) {
		if (args.length == 6)
		{
			if (!erTall(args[1]) || !erTall(args[2]) || !erTall(args[3]) || !erTall(args[4]))
			{
				// ugyldig tall argumenter
				return false;
			}
			
			// parse argumentene til riktige typer og tegn linjen
			int x1 = Integer.parseInt(args[1]);
			int y1 = Integer.parseInt(args[2]);
			int x2 = Integer.parseInt(args[3]);
			int y2 = Integer.parseInt(args[4]);
			int tegn = args[5].charAt(0);
			
			tegnLinje(new Point(x1, y1), new Point(x2, y2), tegn, lerret, lerretDim);
			
			return true;
		}
		
		return false;
	}
	
	/*
	 * Kommando: tegnPunkt
	 */
	static boolean kommandoTegnPunkt(String[] args, int[] lerret, Rectangle lerretDim) {
		if (args.length == 4)
		{
			if (!erTall(args[1]) || !erTall(args[2]))
			{
				// ugyldig tall argumenter
				return false;
			}
			
			// parse argumentene til riktige typer og tegn linjen
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			int tegn = args[3].charAt(0);
			
			tegnPunkt(x, y, tegn, lerret, lerretDim);
			
			return true;
		}
		
		return false;
	}
	
	/*
	 * Kommando: tegnPunkt
	 */
	static boolean kommandoTegnRektangel(String[] args, int[] lerret, Rectangle lerretDim) {
		if (args.length == 6)
		{
			if (!erTall(args[1]) || !erTall(args[2]) || !erTall(args[3]) || !erTall(args[4]))
			{ // ugyldig argumenter for tall argumentene
				return false;
			}
			
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			int width = Integer.parseInt(args[3]);
			int height = Integer.parseInt(args[4]);
			int tegn = args[5].charAt(0);
			
			tegnRektangel(new Rectangle(x, y, width, height), tegn, lerret, lerretDim);
			
			//success!
			return true;
		}
		
		return false;
	}
	
	/*
	 * Kommando: hjelp
	 */
	static boolean kommandoHjelp(String[] args, int[] lerret, Rectangle lerretDim) {
		if (args.length > 2)
		{ // ugyldig argumenter
			return false;
		}
		else if (args.length == 2)
		{ // skriv bare ut forklaring for 1 kommando
			pKommandoForklaring(args[1]);
		}
		else
		{ // Skriv ut total-hjelpen
			printHjelp();
		}
		
		return true;
	}
	
	/*
	 * Kommando: figur
	 */
	static boolean kommandoFigur(String[] args, int[] lerret, Rectangle lerretDim) {
		if (args.length == 4)
		{
			if (!erTall(args[2]) || !erTall(args[3]))
			{ // ugyldig tall
				return false;
			}
			
			int x = Integer.parseInt(args[2]);
			int y = Integer.parseInt(args[3]);
			
			String figurNavn = args[1].toLowerCase();
			
			if (figurNavn.equals("ifi"))
			{ // IFI logo
				int c = '#';
				tegnPunkt(x+0, y+2, c, lerret, lerretDim);
				tegnPunkt(x+1, y+3, c, lerret, lerretDim);
				tegnPunkt(x+1, y+2, c, lerret, lerretDim);
				tegnPunkt(x+0, y+3, c, lerret, lerretDim);
				
				tegnLinje(new Point(x+0, y+5), new Point(x+5, y+5), c, lerret, lerretDim);
				tegnLinje(new Point(x+0, y+6), new Point(x+5, y+6), c, lerret, lerretDim);
				
				tegnPunkt(x+0, y+7, c, lerret, lerretDim);
				tegnPunkt(x+1, y+8, c, lerret, lerretDim);
				tegnPunkt(x+1, y+7, c, lerret, lerretDim);
				tegnPunkt(x+0, y+8, c, lerret, lerretDim);
				
				tegnPunkt(x+0, y+9, c, lerret, lerretDim);
				tegnPunkt(x+1, y+10, c, lerret, lerretDim);
				tegnPunkt(x+1, y+9, c, lerret, lerretDim);
				tegnPunkt(x+0, y+10, c, lerret, lerretDim);
				
				tegnPunkt(x+2, y+10, c, lerret, lerretDim);
				tegnPunkt(x+3, y+11, c, lerret, lerretDim);
				tegnPunkt(x+1, y+11, c, lerret, lerretDim);
				tegnPunkt(x+2, y+11, c, lerret, lerretDim);
				
				tegnPunkt(x+4, y+11, c, lerret, lerretDim);
				tegnPunkt(x+5, y+12, c, lerret, lerretDim);
				tegnPunkt(x+5, y+11, c, lerret, lerretDim);
				tegnPunkt(x+4, y+12, c, lerret, lerretDim);
				tegnPunkt(x+3, y+12, c, lerret, lerretDim);
				tegnPunkt(x+2, y+12, c, lerret, lerretDim);
				
				/**************************/
				
				tegnLinje(new Point(x+8, y+3), new Point(x+8, y+12), c, lerret, lerretDim);
				tegnLinje(new Point(x+9, y+3), new Point(x+9, y+12), c, lerret, lerretDim);
				
				tegnPunkt(x+13, y+0, c, lerret, lerretDim);
				tegnPunkt(x+14, y+0, c, lerret, lerretDim);
				tegnPunkt(x+13, y+1, c, lerret, lerretDim);
				tegnPunkt(x+14, y+1, c, lerret, lerretDim);
				
				tegnPunkt(x+11, y+0, c, lerret, lerretDim);
				tegnPunkt(x+12, y+0, c, lerret, lerretDim);
				tegnPunkt(x+11, y+1, c, lerret, lerretDim);
				tegnPunkt(x+12, y+1, c, lerret, lerretDim);
				
				tegnPunkt(x+10, y+2, c, lerret, lerretDim);
				tegnPunkt(x+10, y+1, c, lerret, lerretDim);
				tegnPunkt(x+10, y+0, c, lerret, lerretDim);
				tegnPunkt(x+10, y+2, c, lerret, lerretDim);
				tegnPunkt(x+9, y+2, c, lerret, lerretDim);
				tegnPunkt(x+8, y+2, c, lerret, lerretDim);
				tegnPunkt(x+9, y+1, c, lerret, lerretDim);
				
				/**************************/
				
				tegnLinje(new Point(x+10, y+5), new Point(x+19, y+5), c, lerret, lerretDim);
				tegnLinje(new Point(x+10, y+6), new Point(x+19, y+6), c, lerret, lerretDim);
				
				tegnPunkt(x+18, y+3, c, lerret, lerretDim);
				tegnPunkt(x+18, y+2, c, lerret, lerretDim);
				tegnPunkt(x+19, y+3, c, lerret, lerretDim);
				tegnPunkt(x+19, y+2, c, lerret, lerretDim);
				
				tegnPunkt(x+18, y+7, c, lerret, lerretDim);
				tegnPunkt(x+18, y+8, c, lerret, lerretDim);
				tegnPunkt(x+19, y+7, c, lerret, lerretDim);
				tegnPunkt(x+19, y+8, c, lerret, lerretDim);
				
				tegnPunkt(x+18, y+9, c, lerret, lerretDim);
				tegnPunkt(x+18, y+10, c, lerret, lerretDim);
				tegnPunkt(x+19, y+9, c, lerret, lerretDim);
				tegnPunkt(x+19, y+10, c, lerret, lerretDim);
				
				tegnPunkt(x+14, y+11, c, lerret, lerretDim);
				tegnPunkt(x+14, y+12, c, lerret, lerretDim);
				tegnPunkt(x+15, y+11, c, lerret, lerretDim);
				tegnPunkt(x+15, y+12, c, lerret, lerretDim);
				
				tegnPunkt(x+16, y+11, c, lerret, lerretDim);
				tegnPunkt(x+16, y+12, c, lerret, lerretDim);
				tegnPunkt(x+17, y+11, c, lerret, lerretDim);
				tegnPunkt(x+17, y+12, c, lerret, lerretDim);
				tegnPunkt(x+15, y+12, c, lerret, lerretDim);
				
				tegnPunkt(x+17, y+10, c, lerret, lerretDim);
				tegnPunkt(x+18, y+11, c, lerret, lerretDim);
			}
			else if (figurNavn.equals("windows"))
			{ // Windows logo
				tegnLinje(new Point(x+0, y+0), new Point(x+0, y+3), 'o', lerret, lerretDim);
				tegnLinje(new Point(x+1, y+0), new Point(x+1, y+3), 'o', lerret, lerretDim);
				tegnLinje(new Point(x+2, y+0), new Point(x+2, y+3), 'o', lerret, lerretDim);
				tegnLinje(new Point(x+3, y+0), new Point(x+3, y+3), 'o', lerret, lerretDim);
				
				tegnLinje(new Point(x+0, y+5), new Point(x+0, y+8), '*', lerret, lerretDim);
				tegnLinje(new Point(x+1, y+5), new Point(x+1, y+8), '*', lerret, lerretDim);
				tegnLinje(new Point(x+2, y+5), new Point(x+2, y+8), '*', lerret, lerretDim);
				tegnLinje(new Point(x+3, y+5), new Point(x+3, y+8), '*', lerret, lerretDim);
				
				tegnLinje(new Point(x+5, y+0), new Point(x+5, y+3), '+', lerret, lerretDim);
				tegnLinje(new Point(x+6, y+0), new Point(x+6, y+3), '+', lerret, lerretDim);
				tegnLinje(new Point(x+7, y+0), new Point(x+7, y+3), '+', lerret, lerretDim);
				tegnLinje(new Point(x+8, y+0), new Point(x+8, y+3), '+', lerret, lerretDim);
				
				tegnLinje(new Point(x+5, y+5), new Point(x+5, y+8), '-', lerret, lerretDim);
				tegnLinje(new Point(x+6, y+5), new Point(x+6, y+8), '-', lerret, lerretDim);
				tegnLinje(new Point(x+7, y+5), new Point(x+7, y+8), '-', lerret, lerretDim);
				tegnLinje(new Point(x+8, y+5), new Point(x+8, y+8), '-', lerret, lerretDim);
			}
			else if (figurNavn.equals("hjerte"))
			{ // Et hjerte figur
				tegnSirkel(x+2, y+2, 2, '#', lerret, lerretDim);
				tegnSirkel(x+6, y+2, 2, '#', lerret, lerretDim);
				tegnLinje(new Point(x+0, y+4), new Point(x+4, y+8), '#', lerret, lerretDim);
				tegnLinje(new Point(x+8, y+4), new Point(x+4, y+8), '#', lerret, lerretDim);
				
				tegnRektangel(new Rectangle(x+1, y+1, 2, 2), '#', lerret, lerretDim);
				tegnRektangel(new Rectangle(x+5, y+1, 2, 2), '#', lerret, lerretDim);
				tegnPunkt(x+2, y+2, '#', lerret, lerretDim);
				tegnPunkt(x+6, y+2, '#', lerret, lerretDim);
				
				tegnPunkt(x+4, y+4, '#', lerret, lerretDim);
				tegnPunkt(x+4, y+7, '#', lerret, lerretDim);
				tegnLinje(new Point(x+2, y+5), new Point(x+6, y+5), '#', lerret, lerretDim);
				tegnLinje(new Point(x+3, y+6), new Point(x+5, y+6), '#', lerret, lerretDim);
			}
			else if (figurNavn.equals("java"))
			{
				// the J
				tegnLinje(new Point(x+1, y+0), new Point(x+1, y+9), '#', lerret, lerretDim);
				tegnPunkt(x+0, y+10, '#', lerret, lerretDim);
				
				// the A
				tegnLinje(new Point(x+7, y+4), new Point(x+7, y+8), '#', lerret, lerretDim);
				tegnPunkt(x+7, y+3, '#', lerret, lerretDim);
				tegnPunkt(x+6, y+2, '#', lerret, lerretDim);
				tegnPunkt(x+5, y+2, '#', lerret, lerretDim);
				tegnPunkt(x+4, y+2, '#', lerret, lerretDim);
				
				tegnPunkt(x+4, y+8, '#', lerret, lerretDim);
				tegnPunkt(x+5, y+8, '#', lerret, lerretDim);
				tegnPunkt(x+6, y+7, '#', lerret, lerretDim);
				tegnPunkt(x+3, y+7, '#', lerret, lerretDim);
				tegnPunkt(x+3, y+6, '#', lerret, lerretDim);
				tegnPunkt(x+4, y+5, '#', lerret, lerretDim);
				tegnPunkt(x+5, y+5, '#', lerret, lerretDim);
				tegnPunkt(x+6, y+5, '#', lerret, lerretDim);
				
				// the V
				tegnLinje(new Point(x+9, y+2), new Point(x+11, y+8), '#', lerret, lerretDim);
				tegnLinje(new Point(x+13, y+2), new Point(x+11, y+8), '#', lerret, lerretDim);
				
				// the second A
				tegnLinje(new Point(x+19, y+4), new Point(x+19, y+8), '#', lerret, lerretDim);
				tegnPunkt(x+19, y+3, '#', lerret, lerretDim);
				tegnPunkt(x+18, y+2, '#', lerret, lerretDim);
				tegnPunkt(x+17, y+2, '#', lerret, lerretDim);
				tegnPunkt(x+16, y+2, '#', lerret, lerretDim);
				
				tegnPunkt(x+16, y+8, '#', lerret, lerretDim);
				tegnPunkt(x+17, y+8, '#', lerret, lerretDim);
				tegnPunkt(x+18, y+7, '#', lerret, lerretDim);
				tegnPunkt(x+15, y+7, '#', lerret, lerretDim);
				tegnPunkt(x+15, y+6, '#', lerret, lerretDim);
				tegnPunkt(x+16, y+5, '#', lerret, lerretDim);
				tegnPunkt(x+17, y+5, '#', lerret, lerretDim);
				tegnPunkt(x+18, y+5, '#', lerret, lerretDim);
			}
			
			return true;
		}
		
		return false;
	}
	
	
	/*
	 * INPUT/OUTPUT
	 */
	
	
	/**
	 * Printer ugyldig kommando.
	 * 
	 * @param kommandoNavn Navnet paa den ugyldige kommandoen.
	 */
	static void pUgyldigKommando(String kommandoNavn) {
		System.out.println("Invalid command: " + kommandoNavn);
	}
	
	/**
	 * Printer ugyldig kommando argumenter.
	 * 
	 * @param kommandoNavn Navnet paa kommandoen.
	 */
	static void pUgyldigArgumenter(String kommandoNavn) {
		System.out.println("Invalid arguments for " + kommandoNavn + ".");
	}
	
	/**
	 * Printer en forklaring av den gitte kommandoen.
	 * 
	 * @param kommandoNavn Kommandoet sitt navn.
	 */
	static void pKommandoForklaring(String kommandoNavn) {
		String[] cmdListe = kommandoListe();
		boolean cmdFound = false;
		
		// bruk array strukturen som forklart i javadoc-en
		// til kommandoListe().
		for (int i = 0; i < cmdListe.length; i+=2)
		{
			if (cmdListe[i].equals(kommandoNavn))
			{
				System.out.println(kommandoNavn + ": " + cmdListe[i + 1]);
				
				// gaa ut av lokken
				cmdFound = true;
				break;
			}
		}
		
		// kommandoen fantes ikke
		if (!cmdFound)
		{
			System.out.println(kommandoNavn + ": cannot find this command.");
		}
	}
	
	/**
	 * Skriver ut en liste over alle kommandoene i programmet.
	 * og forklaringen dems.
	 */
	static void printHjelp() {
		String[] cmdListe = kommandoListe();
		
		System.out.println("\nCommands are in the following format: commandName argument1, argument2, ...");
		System.out.println("List of Commands: \n");
		
		// Skriv ut hver kommando og forklaringen dems.
		for (int i = 0; i < cmdListe.length; i+=2)
		{
			System.out.println(cmdListe[i] + ":");
			System.out.println("  " + cmdListe[i+1]);
		}
	}
	
	/**
	 * Faar hoyde og lengde til lerretet fra brukeren.
	 * 
	 * @param innleser Scanner objektet som skal brukes.
	 * @return
	 */
	static Rectangle faaDimensjoner(Scanner innleser) {
		System.out.print("Canvas Height (in pixels): ");
		int height = Integer.parseInt(innleser.nextLine());
		System.out.print("Canvas Width (in pixels): ");
		int width = Integer.parseInt(innleser.nextLine());
		
		return new Rectangle(width, height);
	}
	
	/**
	 * Printer ut lerretet i terminalen.
	 * 
	 * @param lerret Lerret arrayen.
	 * @param lerretDim Lerret dimensjonen.
	 */
	static void printLerret(int[] lerret, Rectangle lerretDim) {
		for (int i = 0; i < lerret.length; i++)
		{
			if (i != 0 && i % lerretDim.width == 0)
			{
				System.out.println();
			}
			
			// Skriver ut dobbelt for aa kompansere for hoyden paa fonten
			System.out.print((char)lerret[i] + "" + (char)lerret[i]);
		}
	}
	
	/**
	 * Haandterer input/output syklusen.
	 * 
	 * @param innleser Scanner objektet som skal brukes.
	 * @param lerret Lerret data arrayen.
	 * @param lerretDim Lerret dimensjoner.
	 */
	static void ioLoop(Scanner innleser, int[] lerret, Rectangle lerretDim) {
		boolean exit = false;

		// skriv ut litt informasjon
		System.out.println("You are drawing on a canvas which is " + lerretDim.width + "x" + lerretDim.height + " big.");
		System.out.println("NOTE: The point (0, 0) is the furthest top-left pixel, and bigger X is more to the right and bigger Y is more downwards.\n");
		System.out.println("If you need any help, write 'help' and press enter.");
		
		while (!exit)
		{
			System.out.print("Command: ");
			String plainCmd = innleser.nextLine();
			
			if (plainCmd.trim().startsWith("exit"))
			{ // lukk programmet hvis "exit" er utfort.
				exit = true;
			}
			else if (plainCmd.trim().startsWith("eraseAll"))
			{
				viskAlt(lerret);
				printLerret(lerret, lerretDim);
			}
			else
			{
				// sjekk kommando og kjor hvis funnet riktig
				String[] cmdArgs = parseKommando(plainCmd);
				kjorKommando(cmdArgs, lerret, lerretDim);
			}
			
			// bare for aesthetics' skyld
			System.out.println();
		}
	}
	
	
	/*******************************************/
	
	
	public static void main(String[] args) {
		Scanner innleser = new Scanner(System.in);
		
		Rectangle lerretDimensjoner = faaDimensjoner(innleser);
		int[] lerret = new int[lerretDimensjoner.height*lerretDimensjoner.width];
		
		// initialiser alt til blankt.
		viskAlt(lerret);
		
		// start input/output syklysen
		ioLoop(innleser, lerret, lerretDimensjoner);
	}
}
