package com.example.guanzhuli.icart.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class CompraLinhaData implements Serializable {
	
	
    private Integer idProduto;
    private String nomeProduto;
    private Double preco;
    private Double precoPack;
    private String porcao;
    private String porcaoPack;
    private Integer quantidade;
    private Integer quantidadePack;
    private Integer quantidadeMinima;
    private Integer versao;    
	private String descricao;

	public final static BigDecimal IVA = new BigDecimal(0.06);

	public Integer getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}
	
	public String getPorcao() {
		return porcao;
	}
	public void setPorcao(String porcao) {
		this.porcao = porcao;
	}
	public String getPorcaoPack() {
		return porcaoPack;
	}
	public void setPorcaoPack(String porcaoPack) {
		this.porcaoPack = porcaoPack;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getQuantidadePack() {
		return quantidadePack;
	}

	public void setQuantidadePack(Integer quantidadePack) {
		this.quantidadePack = quantidadePack;
	}

	
	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Double getPrecoPack() {
		return precoPack;
	}

	public void setPrecoPack(Double precoPack) {
		this.precoPack = precoPack;
	}

	public Integer getQuantidadeMinima() {
		return quantidadeMinima;
	}

	public void setQuantidadeMinima(Integer quantidadeMinima) {
		this.quantidadeMinima = quantidadeMinima;
	}


	public CompraLinhaData(Integer idProduto, Integer versao, String nomeProduto, Double preco, Double precoPack,
						   String porcao, String porcaoPack, Integer quantidadeMinima, Integer quantidade, Integer quantidadePack, String descricao) {
		super();
		this.idProduto = idProduto;
		this.versao = versao;
		this.nomeProduto = nomeProduto;
		this.preco = preco;
		this.precoPack = precoPack;
		this.porcao = porcao;
		this.porcaoPack = porcaoPack;
		this.quantidadeMinima = quantidadeMinima;
		this.quantidade = quantidade;
		this.quantidadePack = quantidadePack;
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return "CompraLinhaData{" +
				"idProduto=" + idProduto +
				", nomeProduto='" + nomeProduto + '\'' +
				", preco=" + preco +
				", precoPack=" + precoPack +
				", porcao='" + porcao + '\'' +
				", porcaoPack='" + porcaoPack + '\'' +
				", quantidade=" + quantidade +
				", quantidadePack=" + quantidadePack +
				", quantidadeMinima=" + quantidadeMinima +
				", versao=" + versao +
				", descricao='" + descricao + '\'' +
				'}';
	}

	public CompraLinhaData(){
		super();
	}
	
}
