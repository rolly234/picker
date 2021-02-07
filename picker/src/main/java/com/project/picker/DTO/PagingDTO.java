package com.project.picker.DTO;

public class PagingDTO {
	
	private int pageNum;	// ���� ������
	private int rowCount; 	// �� ����
	private int rowSize; 	// �� ������
	private int startRow; 	// ������
	private int endRow; 	// ����
	private int pageCount; 	// ������ ����
	private int pageSize; 	// ������ ������
	private int startPage;	// ���� ������
	private int endPage;	// �� ������
	
	public PagingDTO() { }
	
	public PagingDTO(int pageNum, int rowCount, int rowSize, int pageSize) {
		this.pageNum = pageNum;
		this.rowCount = rowCount;
		this.rowSize = rowSize;
		this.startRow = ((pageNum - 1) * rowSize) + 1;
		this.endRow = pageNum * rowSize;
		this.pageCount = (rowCount / rowSize) + (rowCount % rowSize == 0 ? 0 : 1);
		this.pageSize = pageSize;
		this.startPage = (((pageNum / pageSize) + (pageNum % pageSize == 0 ? -1 : 0)) * pageSize) + 1;
		this.endPage = (this.startPage + pageSize) - 1 > this.pageCount ? this.pageCount : (this.startPage + pageSize) - 1;
	}

	public int getPageNum() {
		return pageNum;
	}
	public int getRowCount() {
		return rowCount;
	}
	public int getRowSize() {
		return rowSize;
	}
	public int getStartRow() {
		return startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public int getPageCount() {
		return pageCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public int getStartPage() {
		return startPage;
	}
	public int getEndPage() {
		return endPage;
	}

}
