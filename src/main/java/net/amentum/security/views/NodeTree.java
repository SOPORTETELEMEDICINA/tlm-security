package net.amentum.security.views;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * @author dev06
 */
public class NodeTree implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7137618952944195524L;
	private String content;
    private List<NodeTree> children;
    private Long id;
    @JsonIgnore
    private Long parentId;
    private Boolean active;
    private Boolean module;
    private Boolean expanded = false;
    @JsonIgnore
    private Integer childCount = 0;
    @JsonIgnore
    private Integer childSelected = 0;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<NodeTree> getChildren() {
        return children;
    }

    public void setChildren(List<NodeTree> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getModule() {
        return module;
    }

    public void setModule(Boolean module) {
        this.module = module;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public Integer getChildSelected() {
        return childSelected;
    }

    public void setChildSelected(Integer childSelected) {
        this.childSelected = childSelected;
    }

    @Override
    public String toString() {
        return "NodeTree{" +
                "content='" + content + '\'' +
                ", children=" + children +
                ", id=" + id +
                ", parentId=" + parentId +
                ", active=" + active +
                ", module=" + module +
                ", expanded=" + expanded +
                ", childCount=" + childCount +
                ", childSelected=" + childSelected +
                '}';
    }
}
