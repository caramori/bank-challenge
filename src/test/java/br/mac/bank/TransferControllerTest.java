package br.mac.bank;

import br.mac.bank.api.dto.mapper.TransferMapper;
import br.mac.bank.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransferServiceImpl service;

	@MockBean
	private TransferMapper transferMapper;

	@Test
	public void shouldBeForbidden() throws Exception {
		this.mockMvc.perform(post("/api/transfer"))
					.andExpect(status().isForbidden());
	}
}