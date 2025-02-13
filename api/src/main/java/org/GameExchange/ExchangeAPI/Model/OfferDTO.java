package org.GameExchange.ExchangeAPI.Model;

import java.util.List;

public class OfferDTO {
    private List<Integer> requested;

    private List<Integer> offered;

    private int requesteeId;

    

    public OfferDTO() {
    }
    

    public OfferDTO(List<Integer> requested, List<Integer> offered, int requestee) {
        this.requested = requested;
        this.offered = offered;
        this.requesteeId = requestee;
    }


    public int getRequesteeId(){
        return requesteeId;
    }

    public List<Integer> getRequested() {
        return requested;
    }

    public List<Integer> getOffered() {
        return offered;
    }

    
    public void setRequested(List<Integer> requested) {
        this.requested = requested;
    }


    public void setOffered(List<Integer> offered) {
        this.offered = offered;
    }


    public void setRequesteeId(int requesteeId) {
        this.requesteeId = requesteeId;
    }

    


    @Override
    public String toString() {
        return "OfferDTO [requested=" + requested + ", offered=" + offered + ", requesteeId=" + requesteeId + "]";
    }


}