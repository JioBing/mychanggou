package entity;

import java.util.List;


/**
 * @ClassName PageResult
 * @Description 分页结果返回类
 * @Author huangpengbing
 * @Date 2024/8/20 2:48
 * @Version 1.0
 **/

public class PageResult<T> {

    private Long total;//总记录数
    private List<T> rows;//记录

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageResult() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
