package medplanner.services;

import medplanner.dto.LocacaoDTO;
import medplanner.model.Locacao;
import medplanner.model.Usuario;
import medplanner.repository.AlaRepository;
import medplanner.repository.LocacaoRepository;
import medplanner.repository.SalaRepository;
import medplanner.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository locacaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private SalaRepository salaRepository;
    @Autowired
    private AlaRepository alaRepository;

    public Locacao salvarLocacaoGeral(Usuario usuario, LocacaoDTO locacaoDetails) {
//        if (locacaoRepository.existeDataHoraMarcadaNaSala(locacaoDetails.getSala(), locacaoDetails.getHoraInicio(),
//                locacaoDetails.getHoraFinal(), locacaoDetails.getDia())) {
//            throw new IllegalArgumentException("Atenção! Já está registrado uma locação para a sala, data e horário informados!");
//        }

        Locacao locacao = new Locacao();
        locacao.setUsuario(usuario);
        locacao.setAla(alaRepository.findById(locacaoDetails.getAla()).orElseThrow());
        locacao.setSala(salaRepository.findById(locacaoDetails.getSala()).orElseThrow());
        locacao.setDia(locacaoDetails.getDia());
        locacao.setHoraInicio(locacaoDetails.getHoraInicio());
        locacao.setHoraFinal(locacaoDetails.getHoraFinal());
        return locacaoRepository.save(locacao);
    }

    public Locacao salvarLocacao(Long usuarioId, LocacaoDTO locacaoDetails) {
        if (usuarioId == null) {
            Usuario currentUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return salvarLocacaoGeral(currentUser, locacaoDetails);
        } else {
            Usuario user = usuarioRepository.findById(usuarioId).orElseThrow();
            return salvarLocacaoGeral(user, locacaoDetails);
        }
    }

    public Locacao atualizarLocacao(Long idMedico, LocacaoDTO locacaoDetails) {
        Optional<Locacao> locacaoOptional = locacaoRepository.findById(locacaoDetails.getIdLocacao());
        if (!locacaoOptional.isPresent()) {
            throw new IllegalArgumentException("Locação não encontrada!");
        }

        Locacao locacao = locacaoOptional.get();

//        if (locacaoRepository.existeDataHoraMarcadaNaSala(locacaoDetails.getSala(),
//                locacaoDetails.getHoraInicio(), locacaoDetails.getHoraFinal(), locacaoDetails.getDia())) {
//            throw new IllegalArgumentException("Atenção! Já está registrado uma locação para a sala, data e horário informados!");
//        }

        locacao.setUsuario(usuarioRepository.findById(idMedico).orElseThrow());
        locacao.setAla(alaRepository.findById(locacaoDetails.getAla()).orElseThrow());
        locacao.setSala(salaRepository.findById(locacaoDetails.getSala()).orElseThrow());
        locacao.setDia(locacaoDetails.getDia());
        locacao.setHoraInicio(locacaoDetails.getHoraInicio());
        locacao.setHoraFinal(locacaoDetails.getHoraFinal());
        return locacaoRepository.save(locacao);
    }

    public List<Locacao> listarLocacoes() {
        return locacaoRepository.findAll();
    }

    public Optional<Locacao> buscarLocacaoById(Long id) {
        return locacaoRepository.findById(id);
    }

    public List<Locacao> findBySala(String id) {
        return locacaoRepository.findBySala(id);
    }

    public List<Locacao> findByMedico(String id) {
        return locacaoRepository.findByMedico(id);
    }

    public void deletarLocacao(Long id) {
        Optional<Locacao> locacaoOptional = locacaoRepository.findById(id);
        if (!locacaoOptional.isPresent()) {
            throw new IllegalArgumentException("Locação não encontrada!");
        }

        locacaoRepository.delete(locacaoOptional.get());
    }
}
