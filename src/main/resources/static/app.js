const refreshButton = document.querySelector("#refreshButton");
const statusText = document.querySelector("#status");
const transferBody = document.querySelector("#transferBody");
const extractedNodes = document.querySelector("#extractedNodes");
const requiredNodes = document.querySelector("#requiredNodes");
const sourceLine = document.querySelector("#sourceLine");
const generatedAt = document.querySelector("#generatedAt");

refreshButton.addEventListener("click", loadTransfers);
window.addEventListener("load", loadTransfers);

async function loadTransfers() {
    refreshButton.disabled = true;
    statusText.textContent = "Crawling live football value HTML...";

    try {
        const response = await fetch("/api/transfers", { cache: "no-store" });
        const payload = await response.json();

        if (!response.ok) {
            throw new Error(payload.details || payload.message || "Crawler request failed.");
        }

        renderReport(payload);
        statusText.textContent = "Fresh player-value data loaded";
    } catch (error) {
        transferBody.innerHTML = `<tr><td colspan="8" class="empty">${escapeHtml(error.message)}</td></tr>`;
        statusText.textContent = "Crawler failed";
    } finally {
        refreshButton.disabled = false;
    }
}

function renderReport(report) {
    extractedNodes.textContent = report.extractedNodes;
    requiredNodes.textContent = report.requiredNodes;
    sourceLine.textContent = `Source: ${report.source}`;
    generatedAt.textContent = `Generated: ${formatDate(report.generatedAt)}`;

    transferBody.innerHTML = report.items.slice(0, 50).map((item, index) => `
        <tr>
            <td>${index + 1}</td>
            <td><span class="type-pill">${escapeHtml(item.dataType)}</span></td>
            <td><a href="${escapeAttribute(item.url)}" target="_blank" rel="noreferrer">${escapeHtml(item.playerName || item.title)}</a></td>
            <td>${escapeHtml(item.position || "Unknown")}</td>
            <td>${escapeHtml(item.marketValueText || "Unknown")}</td>
            <td>${escapeHtml(item.currentClub || "Unknown club")}</td>
            <td>${escapeHtml(item.transferSignal || item.targetClub || "Transfer watchlist")}</td>
            <td>${item.impactScore.toFixed(1)}</td>
        </tr>
    `).join("");
}

function formatDate(value) {
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
        return value;
    }
    return date.toLocaleString();
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function escapeAttribute(value) {
    return escapeHtml(value).replaceAll("`", "&#096;");
}
