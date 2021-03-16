package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Curso;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest // Para quando quer testar repository
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Para não substituir o banco que
                                                                        // está sendo usado pelo banco default(h2)
@ActiveProfiles("test") // Força o Spring a usar o profile test
public class CursoRepositoryTest {

    @Autowired // Para a injeção de dependências
    private CursoRepository repository;

    @Autowired
    private TestEntityManager em; // Banco de teste

    @Test
    public void deveriaCarregarUmCursoAoBuscarPeloSeuNome(){
        String nomeCurso = "HTML 5";

        Curso html5 = new Curso(); // Para popular o banco que agora está vazio
        html5.setNome(nomeCurso);
        html5.setCategoria("Programaçao");
        em.persist(html5);

        Curso curso = repository.findByNome(nomeCurso);
        Assert.assertNotNull(curso);
        Assert.assertEquals(nomeCurso, curso.getNome()); // Compara p ver se
                                                        // a variável local = a que foi pega em curso
    }
}