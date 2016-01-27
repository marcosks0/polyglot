package polyglot.core;

import java.io.FileNotFoundException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class PolyglotTests {
	
	private Mensagens msgPtBR;
	private Mensagens msgEnUS;
	
	@Before
	public void setUp() {
		msgPtBR = Polyglot.of(Mensagens.class, new Locale("pt", "BR"));
		msgEnUS = Polyglot.of(Mensagens.class, new Locale("en", "US"));
	}

	@Test
	public void testSimpleStringMessage() {
		System.out.println(msgPtBR.usuarioInativo());
		System.out.println(msgEnUS.usuarioInativo());
	}

	@Test
	public void testTemplateStringMessage() {
		System.out.println(msgPtBR.emailAtivacaoEnviadoComSucessoPara()
				.withParameter("enderecoEmailAtivacao", "user@server.com")
				.toString());

		System.out.println(msgEnUS.emailAtivacaoEnviadoComSucessoPara()
				.withParameter("enderecoEmailAtivacao", "user@server.com")
				.toString());
		
	}
	
	@Test(expected = FileNotFoundException.class)
	public void testExceptionMessage() throws Exception {
		throw msgPtBR.arquivoNaoEncontrado("foto.jpg");
	}
	
}
