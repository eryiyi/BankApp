package com.ruiping.BankApp.entiy;

import java.io.Serializable;

public class TreeNode implements Serializable {
	private Integer id;  
    private Integer pId;  
    private String name;  
    private Boolean checked;  
    private Boolean open;  
    /** 
     * @return the id 
     */  
    public Integer getId() {  
        return id;  
    }  
    /** 
     * @param id the id to set 
     */  
    public void setId(Integer id) {  
        this.id = id;  
    }  
    /** 
     * @return the pId 
     */  
    public Integer getPId() {  
        return pId;  
    }  
    /** 
     * @param id the pId to set 
     */  
    public void setPId(Integer id) {  
        pId = id;  
    }  
    /** 
     * @return the name 
     */  
    public String getName() {  
        return name;  
    }  
    /** 
     * @param name the name to set 
     */  
    public void setName(String name) {  
        this.name = name;  
    }  
    /** 
     * @return the checked 
     */  
    public Boolean isChecked() {  
        return checked;  
    }  
    /** 
     * @param checked the checked to set 
     */  
    public void setChecked(Boolean checked) {  
        this.checked = checked;  
    }  
    /** 
     * @return the open 
     */  
    public Boolean isOpen() {  
        return open;  
    }  
    /** 
     * @param open the open to set 
     */  
    public void setOpen(Boolean open) {  
        this.open = open;  
    }  
    public TreeNode(Integer id, Integer pId, String name, Boolean checked, Boolean open) {  
        super();  
        this.id = id;  
        this.pId = pId;  
        this.name = name;  
        this.checked = checked;  
        this.open = open;  
    }  
    public TreeNode() {  
        super();  
    }  
      
}
