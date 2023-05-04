package com.nidyran.rolebasedspringsecurity.service;




import com.nidyran.rolebasedspringsecurity.Exeption.CommandeNotFoundException;
import com.nidyran.rolebasedspringsecurity.Exeption.PanierNotFoundException;
import com.nidyran.rolebasedspringsecurity.Exeption.RestaurantNotFoundException;
import com.nidyran.rolebasedspringsecurity.dao.entity.*;
import com.nidyran.rolebasedspringsecurity.dao.repository.*;
import com.nidyran.rolebasedspringsecurity.enmus.CommandeStatus;
import com.nidyran.rolebasedspringsecurity.service.model.commande.AddCommandeDTO;
import com.nidyran.rolebasedspringsecurity.service.model.commande.CommandeDTO;
import com.nidyran.rolebasedspringsecurity.service.model.commande.CommandeItemDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class CommandeService {
    private final CommandeItemRepository commandeItemRepository;
    private final CommandeRepository commandeRepository;
    private final PanierRepository panierRepository;
    private final ModelMapper modelMapper;
    private final RestaurantRepository restaurantRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final PanierService panierService;

    public AddCommandeDTO createCommande(Long panierId, AddCommandeDTO addCommandeDTO) {
        User user = userDetailsService.getUserById(addCommandeDTO.getUserId());
        Restaurant restaurant = restaurantRepository.findById(addCommandeDTO.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(addCommandeDTO.getRestaurantId()));

        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new PanierNotFoundException());

        Commande commande = new Commande();
        commande.setUserId(user.getId());
        commande.setTotal(panier.getTotal());
        commande.setRestaurant(restaurant);
        commande.setAddress(addCommandeDTO.getAddress());
        commande.setNom(addCommandeDTO.getNom());
        commande.setTel(addCommandeDTO.getTel());
        commande.setDescription(addCommandeDTO.getDescription());
        commande.setPaymentMethod(addCommandeDTO.getPaymentMethod());
        commande.setCommandeStatus(String.valueOf(CommandeStatus.PENDING));
        commande=commandeRepository.save(commande);

        List<CommandeItem> commandeItems = new ArrayList<>();
        for (PanierItem panierItem : panier.getPanierItems()) {
            CommandeItem commandeItem = commandeItemRepository.save(convert(panierItem));
            commandeItem.setCommande(commande);
            commandeItems.add(commandeItem);
        }
        commandeRepository.save(commande);
        panierService.clearPanier(panierId);
       return modelMapper.map(commande,AddCommandeDTO.class);
    }


    private CommandeItem convert (PanierItem panierItem)
    {  CommandeItem commandeItem = new CommandeItem();
        commandeItem.setQuantity(panierItem.getQty());
        commandeItem.setMeal(panierItem.getMeal());
        return commandeItem;
    }

    public CommandeDTO updateCommandeStatus(Long commandeId, CommandeStatus nextStatus) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new CommandeNotFoundException(commandeId));
        commande.setCommandeStatus(nextStatus.name());
        Commande updatedCommande = commandeRepository.save(commande);
        return modelMapper.map(updatedCommande, CommandeDTO.class);
    }
    public List<CommandeDTO> getAllCommandes() {
        List<Commande> commandes = commandeRepository.findAll();
        return commandes.stream()
                .map(commande -> modelMapper.map(commande, CommandeDTO.class))
                .collect(Collectors.toList());
    }
    public List<CommandeItemDTO> getCommandeItemsByCommandId(Long commandeId) {
        List<CommandeItem> commandeItems = commandeItemRepository.findByCommandeId(commandeId);

        List<CommandeItemDTO> commandeItemDTOs = commandeItems.stream()
                .map(commandeItem -> modelMapper.map(commandeItem, CommandeItemDTO.class))
                .collect(Collectors.toList());

        return commandeItemDTOs;
    }
}
