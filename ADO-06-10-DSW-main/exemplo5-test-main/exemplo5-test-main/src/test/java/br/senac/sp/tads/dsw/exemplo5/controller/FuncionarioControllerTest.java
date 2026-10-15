package br.senac.sp.tads.dsw.exemplo5.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.senac.sp.tads.dsw.exemplo5.model.Departamento;
import br.senac.sp.tads.dsw.exemplo5.model.Funcionario;
import br.senac.sp.tads.dsw.exemplo5.repository.DepartamentoRepository;
import br.senac.sp.tads.dsw.exemplo5.repository.FuncionarioRepository;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional 
public class FuncionarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Departamento departamentoPadrao;

    @BeforeEach
    void setUp() {
        Departamento departamento = new Departamento();
        departamento.setNome("Desenvolvimento");
        departamento.setOrcamento(200000.00);
        departamentoPadrao = departamentoRepository.save(departamento);
    }

    @Test
    void deveCriarFuncionarioComSucesso() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("João Silva");
        funcionario.setDataAdmissao(java.time.LocalDate.of(2023, 1, 15));
        funcionario.setDepartamento(departamentoPadrao);

        mockMvc.perform(post("/api/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(funcionario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.dataAdmissao").value("2023-01-15"))
                .andExpect(jsonPath("$.departamento.id").value(departamentoPadrao.getId()));
    }

    @Test
    void deveRetornarErro400AoCriarFuncionarioComDataFutura() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Maria Souza");
        funcionario.setDataAdmissao(java.time.LocalDate.now().plusDays(1)); 
        funcionario.setDepartamento(departamentoPadrao);

        mockMvc.perform(post("/api/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(funcionario)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deveListarTodosOsFuncionarios() throws Exception {
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setNome("Carlos Pereira");
        funcionario1.setDataAdmissao(java.time.LocalDate.of(2022, 5, 10));
        funcionario1.setDepartamento(departamentoPadrao);
        funcionarioRepository.save(funcionario1);

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Ana Lima");
        funcionario2.setDataAdmissao(java.time.LocalDate.of(2021, 3, 20));
        funcionario2.setDepartamento(departamentoPadrao);
        funcionarioRepository.save(funcionario2);

        mockMvc.perform(get("/api/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    void deveBuscarFuncionarioPorId() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Paulo Mendes");
        funcionario.setDataAdmissao(java.time.LocalDate.of(2020, 7, 5));
        funcionario.setDepartamento(departamentoPadrao);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        mockMvc.perform(get("/api/funcionarios/{id}", funcionarioSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(funcionarioSalvo.getId()))
                .andExpect(jsonPath("$.nome").value("Paulo Mendes"))
                .andExpect(jsonPath("$.dataAdmissao").value("2020-07-05"))
                .andExpect(jsonPath("$.departamento.id").value(departamentoPadrao.getId()));

    }

    @Test
    void deveAtualizarFuncionario() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Lucas Oliveira");
        funcionario.setDataAdmissao(java.time.LocalDate.of(2019, 11, 15));
        funcionario.setDepartamento(departamentoPadrao);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        Funcionario funcionarioAtualizado = new Funcionario();
        funcionarioAtualizado.setNome("Lucas Oliveira Atualizado");
        funcionarioAtualizado.setDataAdmissao(java.time.LocalDate.of(2019, 11, 15));
        funcionarioAtualizado.setDepartamento(departamentoPadrao);

        mockMvc.perform(put("/api/funcionarios/{id}", funcionarioSalvo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(funcionarioAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(funcionarioSalvo.getId()))
                .andExpect(jsonPath("$.nome").value("Lucas Oliveira Atualizado"))
                .andExpect(jsonPath("$.dataAdmissao").value("2019-11-15"))
                .andExpect(jsonPath("$.departamento.id").value(departamentoPadrao.getId()));

    }

    @Test 
    void deveApagarFuncionario() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Fernanda Costa");
        funcionario.setDataAdmissao(java.time.LocalDate.of(2018, 2, 28));
        funcionario.setDepartamento(departamentoPadrao);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        mockMvc.perform(delete("/api/funcionarios/{id}", funcionarioSalvo.getId()))
                .andExpect(status().isNoContent()); 

    }
}
