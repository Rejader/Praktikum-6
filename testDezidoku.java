class testDezidoku {
	public static void main (String args []) {
		Dezidoku.Variante v = Dezidoku.Variante.normal;
		if (args.length > 0 && args[0].equals("-x"))
		    v = Dezidoku.Variante.mitDiagonalen;
		Dezidoku s = new Dezidoku(v);
	
		s.read();
		s.write();
		if (s.check())
			s.solve();
		else
			System.out.println("nicht ok");
	}
}