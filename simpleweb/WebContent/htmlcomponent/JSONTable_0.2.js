var jsonTable = {
	init : {
		// <table id='tabId'></table>
		tabId : '',
		// title json, data array json
		jData : {
			title : {},data : []
		},
		dataFormat : {
		// colKey : function(rowJSON, colKey){}
		}
	},
	construct : function() {
		var init = jsonTable.init;
		var tabId = init.tabId,
		colOrder = [],
		tab = $("#" +tabId);
		
		var c = {
			validate : (function() {
				// jsonTable.jdata.title
				return true;
			})(),
			tHead : function() {
			        //construct title.
			        var thead = '<thead><tr>';
			        var title = init.jData.title;
			        for(var titleKey in title){
			        	thead = thead.concat("<th>" + title[titleKey] + "</th>");
			        	colOrder.push(titleKey);
			        }
			        thead = thead.concat('</tr></thead>');
			        tab.append(thead);
			},
			tBody : function() {
			       //construct body.
		        var tabBody = $('<tbody></tbody>');
		        for(var i = 0; i < init.jData.data.length; i++){
		            var tr = $('<tr></tr>');
		        	var row = init.jData.data[i];
		        	for(var j=0; j < colOrder.length; j++){
		        		var colKey = colOrder[j];
		        		var  val = row[colKey];
		        		
		        		var tdHtml = "";
		        		if(val){
		        			if(typeof init.dataFormat != "undefined"){
		        				var func = init.dataFormat[colKey];		        				
		        				tdHtml = func(row, colKey);
		        			}else {
		        				tdHtml = "<td>" +  val + "</td>";		        				
		        			}
		        		}else{
		        			tdHtml = "<td>" + val == 0 ? "0" : "&nbsp;" + "</td>";
		        		}
		            	tr.append(tdHtml);
		            }
		            tabBody.append(tr);
		        }
		        tab.append(tabBody);
			}
		};
		//c.validate;
		c.tHead();
		c.tBody();
	},
	refreshTBody : function(){
		
	}
};