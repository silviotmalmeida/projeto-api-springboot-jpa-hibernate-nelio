package com.silviotmalmeida.app.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.silviotmalmeida.app.entities.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// classe que representa uma entidade Order
// é serializable para permitir operações de IO
// foi incluída a anotação de identificação como entidade para o JPA
// foi incluída a anotação para mapeamento da tabela tb_order
@Entity
@Table(name = "tb_order")
public class Order implements Serializable {

    // atributo serial (obrigatório em serializables)
    private static final long serialVersionUID = 1L;

    // declaração dos atributos
    //// definindo o id como chave primária autoincrementável
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // definindo o formato ISO8601 como padrão
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant moment;

    // associação nx1 com a entidade User
    // definindo o nome da coluna com a chave estrangeira para user_id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User client;

    // o status do pedido será do tipo inteiro, porém associado ao tipo enumerado
    // OrdeStatus
    private int orderStatus;

    // associação 1xn com a entidade OrderItem
    // definindo o nome do atributo do objeto OrderItem a ser considerado na
    // associação
    @OneToMany(mappedBy = "id.order")
    private Set<OrderItem> items = new HashSet<>();

    // associação 1x1 com a entidade Payment
    // definindo o nome do atributo do objeto Payment a ser considerado na
    // associação, bem como a condição cascade, para manter o mesmo id nas classes
    // principal e dependente
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    // construtor vazio (necessário para o framework)
    public Order() {

    }

    // construtor
    public Order(Long id, Instant moment, User client, OrderStatus orderStatus) {
        this.id = id;
        this.moment = moment;
        this.client = client;
        setOrderStatus(orderStatus);
    }

    // início dos getters e setters
    // ------------------------------------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public OrderStatus getOrderStatus() {
        // retorna o valor decodificado do status do pedido a partir do índice
        return OrderStatus.valueOf(orderStatus);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        // se existir o status do pedido, atribui o valor do índice
        if (orderStatus != null) {
            this.orderStatus = orderStatus.getCode();
        }
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    // fim dos getters e setters
    // ------------------------------------------------------------------

    // hashcode e equals para permitir comparação de objetos
    // ------------------------------------------------------------------
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Order other = (Order) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    // ------------------------------------------------------------------

    // métodos adicionais
    // ------------------------------------------------------------------

    // método que retorna o valor total deste pedido
    public Double getTotal() {

        Double sum = 0.0;

        for (OrderItem x : this.items) {

            sum += x.getSubTotal();
        }
        return sum;
    }

}
